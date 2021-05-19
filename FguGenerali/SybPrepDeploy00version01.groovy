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
def String SBM_Script="SBM_Script"
def String SBM_SYB_DS="SBM_SYB_DS"
def String SBM_SYB_DB="SBM_SYB_DB"
def String SBM_SYB_Login="SBM_SYB_Login"
def String SBM_SYB_Password="SBM_SYB_Password"

// liste des scripts et subdirectory
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
// contenu du fichier de test de connexion
def  cmdLine="""\"Test de Connexion\"
GO"""
// Environnement
// Verification input
if(!(SBM_Environnement.equals("Production") || SBM_Environnement.equals("Pre-Production") || SBM_Environnement.equals("Recette") || SBM_Environnement.equals("Integration"))){
	logger("ERROR","Mauvais Environnement")
	System.exit(1)
}

def String sybEnv=""
// Chargement sybEnv
if (SBM_Environnement.equals("Production")) {
	sybEnv="PRD"
	logger("TRACE","Environnement = "+sybEnv)
}
if (SBM_Environnement.equals("Pre-Production")) {
	sybEnv="PPD"
	logger("TRACE","Environnement = "+sybEnv)
}
if (SBM_Environnement.equals("Recette")) {
	sybEnv="REC"
	logger("TRACE","Environnement = "+sybEnv)
}
if (SBM_Environnement.equals("Integration")) {
	sybEnv="INT"
	logger("TRACE","Environnement = "+sybEnv)
}


// workDir
def String workDir="${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"


// Logfile
def String LogFile="${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
//Surcharge pour Test
// !!!!!!!!!!!!!!!!!!!!!!!!!!
LogFile="C:\\Temp\\LogEclipseSybPrepDeploy.log"


// Purge ${SBM_Depot}/${sybEnv}/${SBM_Projet}
def File OldWorkDir
OldWorkDir=new File("${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}")
logger("INFO","suppression de $OldWorkDir")
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
logger("TRACE","creation de $NewWorkDir")
if(!NewWorkDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $NewWorkDir")
	System.exit(1)
}


// creation repertoire des logs du deploiement

def File LogDir=new File("${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}"+File.separator+"log")
logger("TRACE","creation de $LogDir")
if(!LogDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $LogDir",LogFile)
	System.exit(1)
}






// Download du fichier package
logger("INFO"," scp /products/serena/sas/${SBM_BaselineId}/ORACLE/${SBM_Folder}/${SBM_Package} ${SBM_Depot}\\${SBM_Projet}\\${SBM_BaselineId}\\${SBM_Package} ",LogFile)
logger("TRACE"," --------- copie locale output << input ",LogFile)
/*** simulation du transfert à supprimer ***/
def input = new File("C:\\Temp\\Sources\\MonApplication.zip").newDataInputStream();  def output = new File("${workDir}"+File.separator+"${SBM_Package}.zip").newDataOutputStream()
output << input
 input.close(); output.close()
/*****************************/
 
 
 
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
	 logger("ERROR","Erreur durant la decompression du package ${workDir}"+File.separator+"${SBM_Package}.zip",LogFile)
	 logger("ERROR", e.getMessage(),LogFile)
	 System.exit(1)
 }

logger("INFO","Decompression du package ${SBM_Package}.zip dans ${workDir}"+File.separator+"${SBM_Folder} achevée",LogFile)
 
// suppression du package
tempPackage=new File(workDir+File.separator+"${SBM_Package}.zip")	
	if(tempPackage.exists()){
		if(! tempPackage.delete()){
			logger("ERROR","Impossible de supprimer "+tempPackage.getAbsolutePath())
			System.exit(1) 
		}
	}
logger("INFO","Suppression du package ${tempPackage} achevée",LogFile)
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
		logger("INFO","le fichier "+file+" est valide - size = "+sqlfile.length(),LogFile)
		}
	} else {
	logger("ERROR",+sqlfile+" n'existe pas",LogFile)
	System.exit(1)
	}
	logger("TRACE","verification du repertoire "+subdir)
	// ??? on recupere la variable subdir, mais on ne l'exploite pas pour verifier la présence des répertoire
	sqlsubdir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir)
	logger("TRACE","verification du repertoire "+sqlsubdir)
	if(!sqlsubdir.exists()){
		logger("ERROR","le repertoire "+sqlsubdir+" n'existe pas",LogFile)
		System.exit(1)
	} else {
		logger("TRACE","le repertoire "+sqlsubdir+" existe")
		logger("TRACE","analyse du contenu de : "+sqlfile)
		def String sqlfileContent=sqlfile.text
		def File sqlscript
		sqlfileContent.eachLine { itsql->
			def String sqlline=itsql.trim()
			logger("TRACE","contenu de la ligne : "+sqlline)
			logger("TRACE","test existance script  : "+sqlsubdir+File.separator+sqlline)
			sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir+File.separator+sqlline)
			logger("TRACE","test valorisation full path : ${sqlscript}")
			if(sqlscript.exists()){
				if(sqlscript.length() == 0){
					logger("ERROR","taille de "+sqlscript+" = "+sqlscript.length(),LogFile)
					System.exit(1)
				} else {
				logger("INFO","le fichier "+sqlscript+" est valide - size = "+sqlscript.length(),LogFile)
				}
			} else {
				logger("ERROR","le fichier "+sqlscript+" n'existe pas",LogFile)
				System.exit(1)
			}	
			// ATTENTION SI PRESENCE DE "NOGO" LE "GO" EST DETECTE IMPLEM SIMILAIRE NOLIO
			String lastLine = "";
			sqlscript.eachLine { line->
				lastLine=line.trim()
			}
			if(lastLine.matches("(?i).*GO.*")){
				logger("TRACE","derniere ligne : "+lastLine)
				logger("TRACE","commande 'go' présente en fin de fichier")
			} else {
			logger("TRACE","derniere ligne : "+lastLine)
			logger("ERROR","pas de commande 'go' en fin de fichier",LogFile)
			System.exit(1)
			}
		}
	}
}


logger("INFO","Vérification des éléments de configuration",LogFile)
logger("INFO","Le livrable sera envoyé sur la database ${SBM_SYB_DB} du server ${SBM_SYB_DS}  en tant que ${SBM_SYB_Login}",LogFile)

def File testDir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"tmp")
if(!testDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $testDir",LogFile)
	System.exit(1)
}
logger("TRACE","creation de $testDir ok")
logger("TRACE","creation de "+workDir+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql")
def String testScript= "${workDir}"+File.separator+"${SBM_Folder}"+File.separator+"tmp"+File.separator+"01-test-connexion.sql"
boolean success = new File(testScript).createNewFile()
logger("TRACE","creation de "+testScript+" terminée")
new File(testScript).write(cmdLine)
logger("TRACE","ecriture de '"+testScript+"' ok")



// PAVE EXECUTION SQL PLUS AVEC GESTION CODE RETOUR
// ${SBM_Script}
//isql -S${SBM_SYB_DS}  -D${SBM_SYB_DB}  -U${SBM_SYB_Login}  -P${SBM_SYB_Password}  -i ${testScript}  -w 999999 -o 1>> ${workDir}/log/01-test-connexion.sql.res 2>> ${workDir}/log/01-test-connexion.sql.err"
/*

   if [ $? -ne 0 ] ; then
	  echo "[DATE-TIME][E] Erreur lors de l'execution de la commande sqlplus ${SBM_syb_Login}/${SBM_syb_Password}@${SBM_syb_SID} @${SBM_Depot}/${sybEnv}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/tmp/01-test-connexion.sql";
	  cat "${SBM_Depot}/${sybEnv}/${SBM_Projet}/${SBM_BaselineId}/${SBM_Folder}/log/01-test-connexion.res";
	  exit 1;
   fi
*/

// ATTENTION LA VERIFICATION DU FAILED PORTE SUR LE LOG "RES" PAR SUR LE LOG "ERR"
logger("TRACE","EXEC  : isql -S${SBM_SYB_DS}  -D${SBM_SYB_DB}  -U${SBM_SYB_Login}  -P${SBM_SYB_Password}  -i ${testScript}  -w 999999 -o 1>> ${workDir}/log/01-test-connexion.sql.res 2>> ${workDir}/log/01-test-connexion.sql.err")
logger("INFO","verification des logs 01-test-connexion.sql.res",LogFile)
def  File LogTest=new File("${LogDir}"+File.separator+"01-test-connexion.sql.res")
def String LogContent=LogTest.text
LogContent.eachLine { logtestit->
  def String scriptline=logtestit.trim()
  if(scriptline.matches("(?i).*FAILED.*")){
	logger("TRACE","FAILED PRESENT DANS "+LogTest)
	logger("ERROR","ATTENTION: Erreur dans la chaine de connexion. Vérifier les éléments de configuration.",LogFile)
	 System.exit(1)
  } else {
  	logger("INFO","Test de connexion validé.",LogFile)
  }
}
System.exit(0)