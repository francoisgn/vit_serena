package FguGenerali
//******************************************************* lib_common
//logger

def logger (def String level,def String message,def String outputFileStr=null) {
 def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
 println "$timpeStamp:[$level] $message"
 if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
	 def File outputFile = new File(outputFileStr)
	 outputFile<< "$timpeStamp:[$level] $message\n"
 }
}
//execute sqlplus
def int execute_oracle_sql(def envp=[],def String command){
 
 def cr=0
 try{
	 println "[I] Call sqlplus ....ddffff"	 
	 println "[D] command:$command"

	 
	 StringBuffer sbout = new StringBuffer()
	 StringBuffer sberr = new StringBuffer()
	 def process = command.execute(envp,null)
	 
	 def tout = process.consumeProcessOutputStream(sbout)
	 def terr = process.consumeProcessErrorStream(sberr)
	 
	 process.waitFor() // Wait for the command to finish
	 
	 def value = process.exitValue()
	 
	 if (value== 0){
		 cr=0
		 println sbout.toString()
		 println sberr.toString()
	 }
	 else{
		 cr=1
		 println sbout.toString()
		 println sberr.toString()
	 }
 
 }
 catch(Exception e){
	 cr=1
	 println "[E] Fail to execute_oracle_sql : cmd=$command\n Exception occured :\n"+e.getMessage()
 }
 return cr
}

//******************************************************* lib_common

// ###################################### DEBUT PREP DEPLOY

// Variables SBM
def String SBM_Environnement="Production"
def String SBM_Depot="C:\\Temp\\Sources"
def String SBM_Projet="Projet"
def String SBM_BaselineId="BaselineId"
def String SBM_Package="APOGEE_2013.12-04.zip"
def String SBM_Nom_Log="SBM_Nom_Log"
def String SBM_Folder="Folder"
def String SBM_ORA_Login="ORA_Login"
def String SBM_ORA_Password="ORA_Password"
def String SBM_ORA_SID="ORA_SID"
def String SBM_Script="Script"
def String SBM_ORA_Base="MY_ORA_BASE"
def String SBM_ORA_Home="/products/oracle"
def String SBM_ORA_Path="C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files (x86)\\QuickTime\\QTSystem\\"
def String SBM_ORA_Lib="PATH_LD_LIBRARY_PATH"

/* decommenter pour chargement common_lib dans SRA
${p:lib_groovy}



// Variables SBM
def String SBM_Environnement="${p:SBM_Environnement}"
def String SBM_Depot="${p:SBM_Depot}"
def String SBM_Projet="${p:SBM_Projet}"
def String SBM_BaselineId="${p:SBM_BaselineId}"
def String SBM_Package="${p:SBM_Package}"
def String SBM_Nom_Log="${p:SBM_Nom_Log}"
def String SBM_Folder="${p:SBM_Folder}"
def String SBM_ORA_Login="${p:SBM_ORA_Login}"
def String SBM_ORA_Password="${p:SBM_ORA_Password}"
def String SBM_ORA_SID="${p:SBM_ORA_SID}"
def String SBM_Script="${p:SBM_Script}"
def String SBM_ORA_Base="${p:SBM_ORA_Base}"
def String SBM_ORA_Home="${p:SBM_ORA_Home}"
def String SBM_ORA_Path="${p:SBM_ORA_Path}"
def String SBM_ORA_Lib="${p:SBM_ORA_Lib}"


 */


// workDir
def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"

// variables input SQL related
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
def String cmdLine="select sysdate from dual;"
def String sqlscriptname=""

// Log
def String SqlLogDir=workDir+File.separator+"${SBM_Folder}"+File.separator+"log"
def String LogFile=workDir+File.separator+"log"+File.separator+"${SBM_Nom_Log}"



// Environnement 
// Verification input
if(!(SBM_Environnement.equals("Production") || SBM_Environnement.equals("Pre-Production") || SBM_Environnement.equals("Recette") || SBM_Environnement.equals("Integration"))){
 logger("ERROR","Mauvais Environnement",LogFile)
 System.exit(1)
}

// Chargement oraEnv
def String oraEnv=""
if (SBM_Environnement.equals("Production")) {
	oraEnv="PRD"
} 
if (SBM_Environnement.equals("Pre-Production")) {
	oraEnv="PPD"
}
if (SBM_Environnement.equals("Recette")) {
	oraEnv="REC"
}
if (SBM_Environnement.equals("Integration")) {
	oraEnv="INT"
}
logger("TRACE","Environnement = "+oraEnv)
 
 // decompression du package
logger("INFO","Decompression du package ${workDir}"+File.separator+"${SBM_Package} dans ${workDir}"+File.separator+"${SBM_Folder}",LogFile)
try{
	def ant = new AntBuilder()// create an antbuilder
	ant.unzip(  src:"${workDir}"+File.separator+"${SBM_Package}",
				dest:"${workDir}"+File.separator+"${SBM_Folder}",
				overwrite:"false"
     )
}
catch (Exception e) {
   e.printStackTrace()
   logger("ERROR","Erreur durant la decompression du package ${workDir}"+File.separator+"${SBM_Package}",LogFile)
   logger("ERROR", e.getMessage(),LogFile)
   System.exit(1)
}

logger("INFO","Decompression du package ${workDir}"+File.separator+"${SBM_Package} dans ${workDir}"+File.separator+"${SBM_Folder} achevée",LogFile)


// --- a supprimer pour execution
/*
// suppression du package

tempPackage=new File(workDir+File.separator+"${SBM_Package}")
       if(tempPackage.exists()){
             if(! tempPackage.delete()){
                    logger("ERROR","Impossible de supprimer "+tempPackage.getAbsolutePath())
                    System.exit(1)
             }
       }
logger("INFO","Suppression du package ${tempPackage}",LogFile)
*/
// --- a supprimer pour execution





// creation repertoire des logs SQL 
logger("TRACE","creation repertoire des logs SQL")
if(! new File(SqlLogDir).isDirectory() ){
	if(! new File(SqlLogDir).mkdirs()){
		  logger ("ERROR","Impossible de creer le repertoire "+SqlLogDir,LogFile)
		  System.exit(1)
	}
}

logger("INFO","Debut de la Verification du Livrable",LogFile)




def String s=varL_SQLFiles
def tab1=s.split(",")
tab1.each{
       def tab2=it.split(":")
       def file=tab2[0].trim()
       def subdir=tab2[1].trim()
       def File sqlfile=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+file)
	   logger("TRACE","fichier en cours "+sqlfile.getAbsolutePath())
       if(sqlfile.exists()){
             if(sqlfile.length() == 0){
	            logger("ERROR","taille du fichier = "+sqlfile.length(),LogFile)
	            System.exit(1)
             } else {
                logger("TRACE","le fichier "+sqlfile.getName()+" est valide - size = "+sqlfile.length())
             }
       } else {
          logger("ERROR",sqlfile.getAbsolutePath()+" n'existe pas",LogFile)
          System.exit(1)
       }
       logger("TRACE","verification du repertoire "+subdir)
       sqlsubdir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+subdir)
       logger("TRACE","verification du repertoire "+sqlsubdir)
       if(!sqlsubdir.isDirectory()){
         logger("ERROR","le repertoire "+sqlsubdir+" n'existe pas",LogFile)
         System.exit(1)
       } else {
         logger("TRACE","le repertoire "+sqlsubdir+" existe")
         logger("TRACE","analyse du contenu de : "+sqlfile)
         def String sqlfileContent=sqlfile.text
         sqlfileContent.eachLine { itsql->
         def String sqlline=itsql.trim()
         logger("TRACE","contenu de la ligne : "+sqlline)
         logger("TRACE","test existance script  : "+sqlsubdir+File.separator+sqlline)
         sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+subdir+File.separator+sqlline)
         logger("TRACE","test valorisation full path : ${sqlscript}")
         if(sqlscript.isFile()){
			 sqlscriptname=sqlscript.getName()
			 logger("TRACE","nom du script "+sqlscriptname)
            if(sqlscript.length() == 0){
              logger("ERROR","taille de "+sqlscript+" = "+sqlscript.length(),LogFile)
              System.exit(1)
            } else {
				logger("INFO","le fichier "+sqlscript+" est valide - size = "+sqlscript.length(),LogFile)
                logger("TRACE","scan du contenu de "+sqlscript)
                def String scriptContent=sqlscript.text
                scriptContent.eachLine { scriptit->
                  def String scriptline=scriptit.trim()
				  def boolean nomatches=false
                  if(scriptline.matches("(?i).*NOVALIDATE.*")){
                         logger("TRACE","NOVALIDATE PRESENT DANS "+sqlscript)
                         logger("WARN","ATTENTION: Contacter le demandeur pour validation à l'issue du déploiement",LogFile)
						 nomatches=true
                  }
                  if(scriptline.matches("(?i).*NOROLLBACK.*")){
                         logger("TRACE","NOROLLBACK PRESENT DANS "+sqlscript)
                         logger("WARN","ATTENTION: Sauvegarde de la base à faire. Pas de script de retour arrière",LogFile)
						 nomatches=true
                  }
				  if (nomatches==true){
					  	logger("INFO","le script ne contient ni 'norollback' ni 'novalidate'",LogFile)
				  }

                }
            }

         } else {
            logger("ERROR","le fichier "+sqlscript+" n'existe pas",LogFile)
                           System.exit(1)
         }
      }

   }
}



logger("INFO","Vérification des éléments de configuration",LogFile) 
logger("TRACE","Le livrable sera envoyé sur l'instance ${SBM_ORA_SID}  en tant que ${SBM_ORA_Login}")

// on definie le separator avant la partie SQLPLUS 
def sep=File.separator



// Calcul des var environnement
def Map env=System.getenv()
def  envp=[]
env.each{ key,val->
    if( !(key=="ORACLE_BASE" ||key=="ORACLE_SID"||key=="ORACLE_BASE"||key=="ORACLE_HOME"|| key=="PATH"||key=="LD_LIBRARY_PATH" )) {
             envp.add("$key=${val}")
    }
}
// ajout des var environement specifique Oracle
envp.add("ORACLE_BASE=${SBM_ORA_Base}")
envp.add("ORACLE_SID=${SBM_ORA_SID}")
envp.add("ORACLE_HOME=${SBM_ORA_Home}")
envp.add("PATH=${SBM_ORA_Path}")
envp.add("LD_LIBRARY_PATH=${SBM_ORA_Lib}")






logger("TRACE","chargement de l'environnement et execution du tnsping ")
try{
	def command = "${SBM_Script}; tnsping ${SBM_ORA_SID}"// Create the String
	// --- a supprimer pour execution
	logger("TRACE","cmd: "+command)
	command="echo coucou"
	// --- a supprimer pour execution
	def value=execute_oracle_sql(envp,command)
	logger("TRACE","value=$value")
	logger("TRACE","return code: ${value}")
	if(value==0){
		logger("TRACE","la configuration du serveur intermédiaire est valide - TNSPING SUCCESSFUL")
	} else {
		logger("ERROR","ATTENTION: PB dans la configuration du serveur intermédiaire - echec TNSPING",LogFile)
		System.exit(1)
	}
}
catch (Exception e){
	logger("ERROR",e.getMessage(),LogFile)
	System.exit(1)
		  }

def File testDir=new File("${workDir}"+File.separator+"${SBM_Folder}"+File.separator+"tmp")
if(!testDir.isDirectory()){
	if(!testDir.mkdirs()){
		logger("ERROR","Impossible de creer le repertoire "+testDir.getAbsolutePath(),LogFile)
		System.exit(1)
	}
	logger("TRACE","dossier deja existant : "+testDir.getAbsolutePath())
}
logger("TRACE","creation de $testDir ok") 
logger("TRACE","creation de "+workDir+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql")
def String testScript= "${workDir}"+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql"
boolean success = new File(testScript).createNewFile() 
logger("TRACE","creation de "+testScript+" terminée") 
logger("TRACE","ecriture de '"+cmdLine+"' dans "+testScript) 
new File(testScript).write(cmdLine) 
logger("TRACE","ecriture de '"+testScript+"' ok")


try{
	logger("TRACE","execution du script 01-test-connexion.sql")
	def command = "${SBM_Script};sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${workDir}${sep}${SBM_Folder}${sep}tmp${sep}01-test-connexion.sql 1>> ${SqlLogDir}${sep}01-test-connexion.res 2> ${SqlLogDir}${sep}01-test-connexion.err"// Create the String
	// --- a supprimer pour execution
	logger("TRACE","cmd: "+command)
	command="echo coucou"
	// --- a supprimer pour execution
	def value=execute_oracle_sql(envp,command)
	logger("TRACE","value=$value")
	logger("TRACE","return code: ${value}")
	if(value==0){
		logger("TRACE","Test de connexion successful")
	} else {
		logger("ERROR","Echec du test de connexion",LogFile)
		System.exit(1)
	}
}
catch (Exception e){
   logger("ERROR",e.getMessage(),LogFile)
   System.exit(1)
}

System.exit(0)
// ------------ fin step PrepDeploy