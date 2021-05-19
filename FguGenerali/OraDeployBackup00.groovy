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
def String varL_SQLFiles="REWIND.sql:rollback"
// Logfile
def String LogFile="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
//Surcharge pour Test
// !!!!!!!!!!!!!!!!!!!!!!!!!!
LogFile="C:\\Temp\\LogEclipseDeploy.log"

// Environment 
// Verification input Environment
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
	logger("TRACE","Environnement = Pre-Production",LogFile)
}
if (SBM_Environnement.equals("Recette")) {
	def oraEnv="REC"
	logger("TRACE","Environnement = Recette",LogFile)
}
if (SBM_Environnement.equals("Integration")) {
	def oraEnv="INT"
	logger("TRACE","Environnement = Intergration",LogFile)
}
logger("INFO","lancement de la phase de deploiement",LogFile)


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
logger("INFO","creation de $NewWorkDir",LogFile) 

if(!NewWorkDir.mkdirs()){
	   logger("ERROR","Impossible de creer le repertoire "+NewWorkDir.getAbsolutePath())
	   System.exit(1)
}

// creation repertoire des logs du deploiement
// LOGDIR

def File LogDir=new File("${workDir}"+File.separator+"log")
logger("INFO","creation de $LogDir",LogFile) 
if(!LogDir.mkdirs()){
		  logger("ERROR","Impossible de creer le repertoire $LogDir")
		  System.exit(1)
}
// ------------ fin step CreateWorspace


// Download du fichier package
logger("INFO"," scp /products/serena/sas/${SBM_BaselineId}/ORACLE/${SBM_Folder}/${SBM_Package}.zip ${SBM_Depot}\\${SBM_Projet}\\${SBM_BaselineId}\\${SBM_Package} ",LogFile) 
logger("TRACE"," --------- copie locale output << input ",LogFile)
/*** simulation du transfert à supprimer ***/ 
def input = new File("C:\\Temp\\Sources\\MonApplication.zip").newDataInputStream();  def output = new File("${workDir}"+File.separator+"${SBM_Package}.zip").newDataOutputStream()
output << input
input.close(); output.close()
/*****************************/

// ------------ debut step CheckDeployBack
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

// creation repertoire des logs du deploiement - ATTENTION SUBDIR SBM_FOLDER
// LOGDIR

def sep=File.separator

if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").isDirectory() ){
	   if(! new File("${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log").mkdirs()){
			 logger "INFO" "Impossible de creer le repertoire ${SBM_Depot}${sep}${SBM_Projet}${sep}${SBM_BaselineId}${sep}${SBM_Folder}${sep}log"
			 System.exit(1)
	   }
}



def String s=varL_SQLFiles
def tab1=s.split(",")
tab1.each{
	def tab2=it.split(":")
	def file=tab2[0].trim()
	def subdir=tab2[1].trim()
	sqlfile=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+file)
	sqlsubdir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir)

	def String sqlfileContent=sqlfile.text
	sqlfileContent.eachLine { itsql->
		def String sqlline=itsql.trim()
		sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"${SBM_Package}"+File.separator+subdir+File.separator+sqlline)
		reslogFile=new File(workDir+File.separator+"log"+File.separator+sqlline+".res")
		logger("INFO","execution de "+sqlline,LogFile)
		// PAVE EXECUTION SQL PLUS AVEC GESTION CODE RETOUR
		// ${SBM_Script}
		//sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${sqlscript} 1>> "${SBM_Depot}/${workDir}/log/${sqlline}.res" 2> "${SBM_Depot}/${workDir}/log/${sqlline}.err"
		logger("TRACE","EXEC sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${sqlscript} 1>> ${workDir}"+File.separator+"log"+File.separator+"${sqlline}.res 2> ${workDir}"+File.separator+"log"+File.separator+"${sqlline}.err",LogFile)
		logger("INFO","verification des logs "+sqlline+".res",LogFile)
		def String logContent=reslogFile.text
		def Boolean reserrorlog=false
		def Boolean oraerrorlog=false
		def Boolean sperrorlog=false
		def Boolean prompterrorlog=false
		try{
			logContent.eachLine { logit->
				if(oraerrorlog==false){
					if(logit.matches("(?i).*ORA-[0-9]{5}.*")){
						reserrorlog=true
						oraerrorlog=true
					}
				}
				if(sperrorlog==false){
					if(logit.matches("(?i).*SP2-[0-9]{4}.*")){
						reserrorlog=true
						sperrorlog=true
					}
				}
				if(prompterrorlog==false){
					if(logit.matches("(?i).*SQL>.*")){
						reserrorlog=true
						prompterrorlog=true
					}
				}
				if(oraerrorlog==true && sperrorlog==true && prompterrorlog==true){
					throw new Exception("une erreur de chaque type trouvée dans le fichier de log - affichage des alertes")
				}
			}
		}
		catch(Exception e){
			println "WARNING : "+e.getMessage()
		}
		if(reserrorlog==false){
			logger("INFO","analyse de  "+sqlline+".res terminée - pas de warning detecté",LogFile)
			
		} else {
			logger("WARN","analyse de  "+sqlline+".res terminée - warning(s) detecté(s)",LogFile)
			if(oraerrorlog==true){
				logger("WARN","erreur ORA-xxxxx dans la log : "+sqlline+".res",LogFile)
			}
			if(sperrorlog==true){
				logger("WARN","erreur SP2-xxxx dans la log : "+sqlline+".res",LogFile)
			}
			if(prompterrorlog==true){
				logger("WARN","absence de prompt SQL trouvé dans la log : "+sqlline+".res",LogFile)
			}
		}
	}
}
System.exit(0)
