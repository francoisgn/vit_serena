package FguGenerali
// Common function


def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		def File outputFile = new File(outputFileStr)
		outputFile<< "$timpeStamp:[$level] $message\n"
	}
}




// Variables SBM
def String SBM_Environnement="Production"
def String SBM_Depot="C:\\Temp\\Test"
def String SBM_Projet="SBM_Projet"
def String SBM_BaselineId="SBM_BaselineId"
def String SBM_Package="MonApplication"
def String SBM_Nom_Log="SBM_NomLog"
def String SBM_Folder="SBM_Folder"
def String SBM_ORA_Login="SBM_ORA_Login"
def String SBM_ORA_Password="SBM_ORA_Password"
def String SBM_ORA_SID="SBM_ORA_SID"
def String SBM_Script="SBM_Script"




// workDir
def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"
// variables input SQL file
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
// Logfile
def String LogFile="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
//Surcharge pour Test
// !!!!!!!!!!!!!!!!!!!!!!!!!!
def String LogFile="C:\\Temp\\LogEclipsePrepDeploy.log"
// Test Cmd Line
def String cmdLine="select sysdate from dual;"








// Environnement 
// Verification input
if(!(SBM_Environnement.equals("Production") || SBM_Environnement.equals("Pre-Production") || SBM_Environnement.equals("Recette") || SBM_Environnement.equals("Integration"))){
	logger("ERROR","Mauvais Environnement",LogFile)
	System.exit(1)
}

// Chargement oraEnv
if (SBM_Environnement.equals("Production")) {
	def oraEnv="PRD"
	logger("TRACE","Environnement = Production",LogFile)
} 
if (SBM_Environnement.equals("Pre-Production")) {
	def oraEnv="PPD"
	logger("TRACe","Environnement = Pre-Production",LogFile)
}
if (SBM_Environnement.equals("Recette")) {
	def oraEnv="REC"
	logger("TRACE","Environnement = Recette",LogFile)
}
if (SBM_Environnement.equals("Integration")) {
	def oraEnv="INT"
	logger("TRACE","Environnement = Intergration",LogFile)
}


// Purge ${SBM_Depot}/${SBM_Projet}
def File OldWorkDir
OldWorkDir=new File("${SBM_Depot}"+File.separator+"${SBM_Projet}")
logger("INFO","suppression de $OldWorkDir",LogFile)
if(OldWorkDir.exists()){
	if(! OldWorkDir.deleteDir()){
		logger("ERROR","Impossible de supprimer "+OldWorkDir.getAbsolutePath())
		System.exit(1) 
	}
}



	
/*
# ??? pourquoi creer "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Projet}" et pas "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}"
*/

def File NewWorkDir=new File("${workDir}"+File.separator+"${SBM_Projet}")
logger("INFO","creation de $NewWorkDir",LogFile)
if(!NewWorkDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $NewWorkDir",LogFile)
	System.exit(1)
}

// Download du fichier package
logger("INFO"," scp /products/serena/sas/${SBM_BaselineId}/ORACLE/${SBM_Folder}/${SBM_Package} ${SBM_Depot}\\${SBM_Projet}\\${SBM_BaselineId}\\${SBM_Package} ",LogFile)
logger("TRACE"," --------- copie locale output << input ",LogFile)
/*** simulation du transfert à supprimer ***/
def input = new File("C:\\Temp\\Sources\\MonApplication.zip").newDataInputStream();  def output = new File("${workDir}"+File.separator+"${SBM_Package}").newDataOutputStream()
output << input
 input.close(); output.close()
/*****************************/
 
 
 
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

def File LogDir=new File("${workDir}"+File.separator+"log")
logger("INFO","creation de $LogDir",LogFile)
if(!LogDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $LogDir",LogFile)
	System.exit(1)
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
	if(!sqlsubdir.exists()){
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
		if(sqlscript.exists()){
			if(sqlscript.length() == 0){
				logger("ERROR","taille de "+sqlscript+" = "+sqlscript.length(),LogFile)
				System.exit(1)
			} else {
			logger("TRACE","le fichier "+sqlscript+" est valide - size = "+sqlscript.length(),LogFile)
			}
		} else {
		logger("ERROR","le fichier "+sqlscript+" n'existe pas",LogFile)
		System.exit(1)
		}
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
}

logger("INFO","Vérification des éléments de configuration",LogFile)
logger("TRACE","Le livrable sera envoyé sur l'instance ${SBM_ORA_SID}  en tant que ${SBM_ORA_Login}",LogFile)


try{
	def command = """${SBM_Script}; tnsping ${SBM_ORA_SID}"""// Create the String
			 // !!!!!!!!!!!!!!!!!!
			 // a supprimer pour activer l'export
			 command="""cmd /c echo ${command}"""
	// !!!!!!!!!!!!!!!!!!
	logger("TRACE","chargement de l'environnement et execution du tnsping ",LogFile)
	logger("TRACE","cmd:${command}",LogFile)
	
	StringBuffer sbout = new StringBuffer()
	StringBuffer sberr = new StringBuffer()
	// lancement de la commandline dans un thread en background
	def process = command.execute(null, new File(workDir)) // Call *execute* on the string
	// Rq: il est preferable d'utiliser des StringBuffer pour ne pas saturer les buffers de sortie du thread en chrage de la cmmande
	// et d'appeler la method consumeProcessOutputStream et consumeProcessErrorStream
	def tout = process.consumeProcessOutputStream(sbout)
	def terr = process.consumeProcessErrorStream(sberr)
	// attente de terminaison du thread
	 process.waitFor()                               // Wait for the command to finish
   // Obtain status and output
	 def value = process.exitValue()

	 logger("TRACE","return code: ${value}",LogFile)
	 if(value==0){
		  logger("TRACE",'std err: ' +sberr.toString(),LogFile)
		  logger("TRACE",'std out: ' +sbout.toString(),LogFile)
		  logger("TRACE","la configuration du serveur intermédiaire est valide",LogFile)
	 }
	 else{
		 
		 logger("ERROR",'std err: ' +sberr.toString(),LogFile)
		 logger("ERROR",'std out: ' +sbout.toString(),LogFile)
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

/*
 

echo "" "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/tmp/01-test-connexion.sql"
if [ $? -ne 0 ]; then
   echo "[DATE-TIME][E] Impossible de creer un fichier de test de connexion: ${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/tmp/01-test-connexion.sql"
   exit 1
fi
### ??? est ce bien utile?
export ORACLE_SID=
export LD_LIBRARY_PATH=
export PATH=
export ORACLE_HOME=
export ORACLE_BASE=

 cmd /c echo sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/tmp/01-test-connexion.sql 1>> "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/log/01-test-connexion.res" 2> "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/log/01-test-connexion.err"
   
   """
   if [ $? -ne 0 ] ; then 
      echo "[DATE-TIME][E] Erreur lors de l'execution de la commande sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/${oraDir}/${ora_sql_file}"; 
      cat "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/log/01-test-connexion.err"; 
      exit 1;
   fi
   egrep "ORA-[0-9]{5}" "${SBM_Depot}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/log/01-test-connexion.res" 1>/dev/null 2>&1
   if [ $? -eq 0 ]; then 
       echo "[DATE-TIME][E]ATTENTION: Erreur dans la chaine de connexion. Vérifier les éléments de configuration."   >> ${SBM_Nom_Log}
       exit 1
   else
       echo "[DATE-TIME][I]Test de connexion validé"   >> ${SBM_Nom_Log}

   
   fi

echo "######################################"
exit 0
*/


