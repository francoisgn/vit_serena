package FguGenerali
def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		  def File outputFile = new File(outputFileStr)
		  outputFile<< "$timpeStamp:[$level] $message\n"
	}
}

def int execute_oracle_sql(def  envp=[],def String command){

	def cr=0
	try{
			println "[I] Call sqlplus ....ddffff"

			println "[D] command:$command"
			//command="""cmd /c set"""

              StringBuffer sbout = new StringBuffer()
             StringBuffer sberr = new StringBuffer()
             def process = command.execute(envp,null)

             def tout = process.consumeProcessOutputStream(sbout)
             def terr = process.consumeProcessErrorStream(sberr)

               process.waitFor()                               // Wait for the command to finish

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



// ---------------- fin function

// Variables SBM
def String SBM_Environnement="Production"
def String SBM_Depot="C:\\Temp\\Test"
def String SBM_Projet="SBM_Projet"
def String SBM_BaselineId="SBM_BaselineId"
def String SBM_Package="MonApplication"
def String SBM_Nom_Log="SBM_NomLog"
def String SBM_Folder="SBM_Folder"
def String SBM_ORA_Login="SBM_ORA_Login"
def String SBM_Script="SBM_Script"
def String SBM_ORA_Password="SBM_ORA_Password"
def String SBM_ORA_SID="SBM_ORA_SID"
def String SBM_ORA_Base="SBM_ORA_Base"
def String SBM_ORA_Home="SBM_ORA_Home"
def String SBM_ORA_Path="C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;"
def String SBM_ORA_Lib="SBM_ORA_Lib"

// Calcul des variables du script

// variables input SQL file
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
// Test Cmd Line
def String cmdLine="select sysdate from dual;"
// Logfile
def String LogFile="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
// workDir
def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"

// Environnement
// Verification input
if(!(SBM_Environnement.equals("Production") || SBM_Environnement.equals("Pre-Production") || SBM_Environnement.equals("Recette") || SBM_Environnement.equals("Integration"))){
       logger("ERROR","Mauvais Environnement")
       System.exit(1)
}

// Chargement oraEnv
if (SBM_Environnement.equals("Production")) {
       def oraEnv="PRD"
       logger("TRACE","Environnement = Production") } if (SBM_Environnement.equals("Pre-Production")) {
       def oraEnv="PPD"
       logger("TRACe","Environnement = Pre-Production") } if (SBM_Environnement.equals("Recette")) {
       def oraEnv="REC"
       logger("TRACE","Environnement = Recette") } if (SBM_Environnement.equals("Integration")) {
       def oraEnv="INT"
       logger("TRACE","Environnement = Intergration") }


// Purge ${SBM_Depot}/${SBM_Projet}
def File OldWorkDir
OldWorkDir=new File("${SBM_Depot}"+File.separator+"${SBM_Projet}")
logger("INFO","suppression de "+OldWorkDir.getAbsolutePath(),LogFile)
if(OldWorkDir.exists()){
       if(! OldWorkDir.deleteDir()){
             logger("ERROR","Impossible de supprimer "+OldWorkDir.getAbsolutePath())
             System.exit(1)
       }
}


def File NewWorkDir=new File("${workDir}"+File.separator+"${SBM_Projet}")
logger("INFO","creation de $NewWorkDir",LogFile) if(!NewWorkDir.mkdirs()){
       logger("ERROR","Impossible de creer le repertoire "+NewWorkDir.getAbsolutePath())
       System.exit(1)
}

// creation repertoire des logs du deploiement

def File LogDir=new File("${workDir}"+File.separator+"log")
logger("INFO","creation de $LogDir",LogFile) if(!LogDir.mkdirs()){
          logger("ERROR","Impossible de creer le repertoire $LogDir")
          System.exit(1)
}
// ------------ fin step CreateWorspace


// Download du fichier package
logger("INFO"," scp /products/serena/sas/${SBM_BaselineId}/ORACLE/${SBM_Folder}/${SBM_Package}.zip ${SBM_Depot}\\${SBM_Projet}\\${SBM_BaselineId}\\${SBM_Package} ",LogFile) logger("TRACE"," --------- copie locale output << input ",LogFile)
/*** simulation du transfert à supprimer ***/ def input = new File("C:\\Temp\\Sources\\MonApplication.zip").newDataInputStream();  def output = new File("${workDir}"+File.separator+"${SBM_Package}.zip").newDataOutputStream()
output << input
input.close(); output.close()
/*****************************/

// ------------ debut step PrepDeploy
// decompression du package
logger("INFO","Decompression du package ${workDir}"+File.separator+"${SBM_Package} dans ${workDir}"+File.separator+"${SBM_Folder}",LogFile)
try{
       def ant = new AntBuilder()// create an antbuilder

        ant.unzip(  src:"${workDir}"+File.separator+"${SBM_Package}.zip",
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

// suppression du package
tempPackage=new File(workDir+File.separator+"${SBM_Package}"+".zip")
       if(tempPackage.exists()){
             if(! tempPackage.delete()){
                    logger("ERROR","Impossible de supprimer "+tempPackage.getAbsolutePath())
                    System.exit(1)
             }
       }

logger("INFO","Suppression du package ${tempPackage}",LogFile)

// creation repertoire des logs du deploiement

def sep=File.separator

if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").isDirectory() ){
       if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").mkdirs()){
             logger "INFO" "Impossible de creer le repertoire ${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log"
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
       sqlfile=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+file)
       if(sqlfile.exists()){
             if(sqlfile.length() == 0){
                    logger("ERROR","taille de "+sqlfile+" = "+sqlfile.length(),LogFile)
                    System.exit(1)
             } else {
                    logger("TRACE","le fichier "+file+" est valide - size = "+sqlfile.length(),LogFile)
             }
       } else {
              logger("ERROR",+sqlfile+" n'existe pas",LogFile)
              System.exit(1)
       }
       logger("TRACE","verification du repertoire "+subdir,LogFile)
       // ??? on recupere la variable subdir, mais on ne l'exploite pas pour verifier la présence des répertoire
       sqlsubdir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir)
       logger("TRACE","verification du repertoire "+sqlsubdir,LogFile)
       if(!sqlsubdir.isDirectory()){
             logger("ERROR","le repertoire "+sqlsubdir+" n'existe pas",LogFile)
             System.exit(1)
       } else {
             logger("TRACE","le repertoire "+sqlsubdir+" existe",LogFile)
             logger("TRACE","analyse du contenu de : "+sqlfile,LogFile)
             def String sqlfileContent=sqlfile.text
             sqlfileContent.eachLine { itsql->
             def String sqlline=itsql.trim()
             logger("TRACE","contenu de la ligne : "+sqlline,LogFile)
             logger("TRACE","test existance script  : "+sqlsubdir+File.separator+sqlline,LogFile)
             sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir+File.separator+sqlline)
             logger("TRACE","test valorisation full path : ${sqlscript}",LogFile)
             if(sqlscript.isFile()){
                    if(sqlscript.length() == 0){
                           logger("ERROR","taille de "+sqlscript+" = "+sqlscript.length(),LogFile)
                          System.exit(1)
                    } else {
                           logger("INFO","le fichier "+sqlscript+" est valide - size = "+sqlscript.length(),LogFile)
                                        logger("TRACE","scan du contenu de "+sqlscript,LogFile)
                                        // verifier le sens de grep dans nolio
                                        def String scriptContent=sqlscript.text
                                        scriptContent.eachLine { scriptit->
                                                  def String scriptline=scriptit.trim()
                                                  if(scriptline.matches("(?i).*NOVALIDATE.*")){
                                                         logger("TRACE","NOVALIDATE PRESENT DANS "+sqlscript,LogFile)
                                                         logger("WARN","ATTENTION: Contacter le demandeur pour validation à l'issue du déploiement",LogFile)
                                                  }
                                                  if(scriptline.matches("(?i).*NOROLLBACK.*")){
                                                         logger("TRACE","NOROLLBACK PRESENT DANS "+sqlscript,LogFile)
                                                         logger("WARN"," ATTENTION: Sauvegarde de la base à faire. Pas de script de retour arrière",LogFile)
                                                  }

                                        }
                    }

             }
                     else {
            logger("ERROR","le fichier "+sqlscript+" n'existe pas",LogFile)
                           System.exit(1)
             }
          }

       }
}



logger("INFO","Vérification des éléments de configuration",LogFile) logger("TRACE","Le livrable sera envoyé sur l'instance ${SBM_ORA_SID}  en tant que ${SBM_ORA_Login}",LogFile)


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







try{
       def command = """${SBM_Script}; tnsping ${SBM_ORA_SID}"""// Create the String
				 // !!!!!!!!!!!!!!!!!!
				 // a supprimer pour activer l'export
	command="""cmd /c echo ${command}"""
          command="""cmd /c set"""
	// !!!!!!!!!!!!!!!!!!
	logger("TRACE","chargement de l'environnement et execution du tnsping ",LogFile)
	logger("TRACE","cmd:${command}",LogFile)

	def value=execute_oracle_sql(envp,command)
	   logger("TRACE","value=$value",LogFile)

	logger("TRACE","return code: ${value}",LogFile)
	if(value==0){
			logger("TRACE","la configuration du serveur intermédiaire est valide",LogFile)
	}
	else{
		  logger("ERROR","ATTENTION: PB dans la configuration du serveur intermédiaire",LogFile)
		  System.exit(1)
	}
}
catch (Exception e){
	logger("ERROR",e.getMessage(),LogFile)
	System.exit(1)
		  }

def File testDir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"tmp")
if(!testDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $testDir",LogFile)
	System.exit(1)
}
logger("TRACE","creation de $testDir ok",LogFile) 
logger("TRACE","creation de "+workDir+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql",LogFile)
def String testScript= "${workDir}"+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql"
boolean success = new File(testScript).createNewFile() 
logger("TRACE","creation de "+testScript+" terminée",LogFile) 
logger("TRACE","ecriture de '"+cmdLine+"' dans "+testScript,LogFile) 
new File(testScript).write(cmdLine) 
logger("TRACE","ecriture de '"+testScript+"' ok",LogFile)

if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").isDirectory() ){
	if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").mkdirs()){
		  logger "INFO" "Impossible de creer le repertoire ${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log"
		  System.exit(1)
	}
}
try{
	def command = """cmd /c echo sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}tmp${sep}01-test-connexion.sql 1>> "${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log${sep}01-test-connexion.res" 2> "${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log${sep}01-test-connexion.err" """// Create the String
                    // !!!!!!!!!!!!!!!!!!
                    // a supprimer pour activer l'export
       //                                                                    def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"
       // !!!!!!!!!!!!!!!!!!
       logger("TRACE","chargement de l'environnement et execution du tnsping ",LogFile)
       logger("TRACE","cmd:${command}",LogFile)

       def value=execute_oracle_sql(envp,command)
          logger("TRACE","value=$value",LogFile)

       logger("TRACE","return code: ${value}",LogFile)
       if(value==0){
               logger("TRACE","la configuration du serveur intermédiaire est valide",LogFile)
       }
       else{
             logger("ERROR","ATTENTION: PB dans la configuration du serveur intermédiaire",LogFile)
             System.exit(1)
       }
}
catch (Exception e){
       logger("ERROR",e.getMessage(),LogFile)
       System.exit(1)
}

System.exit(0)
// ------------ fin step PrepDeploy
