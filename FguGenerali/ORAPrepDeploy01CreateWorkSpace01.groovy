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

// ###################################### DEBUT CREATE WORKSPACE


// Variables SBM
def String SBM_Environnement="Production"
def String SBM_Depot="C:\\Temp\\Sources"
def String SBM_Projet="Projet"
def String SBM_BaselineId="BaselineId"
def String SBM_Package="Package"
def String SBM_Nom_Log="SBM_Nom_Log"
def String SBM_Folder="Folder"
def String SBM_ORA_Login="ORA_Login"
def String SBM_ORA_Password="ORA_Password"
def String SBM_ORA_SID="ORA_SID"
def String SBM_Script="Script"


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

 */

// workDir
def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"
// variables input SQL file
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
// Log
def String LogDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"+File.separator+"log"
def String LogFile="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"

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
if(OldWorkDir.isDirectory()){
	if(! OldWorkDir.deleteDir()){
		logger("ERROR","Impossible de supprimer "+OldWorkDir.getAbsolutePath())
		System.exit(1)
	}
}



// creation du nouveau work dir vierge
def File NewWorkDir=new File("${workDir}"+File.separator+"${SBM_Projet}")
if(!NewWorkDir.mkdirs()){
	logger("ERROR","Impossible de creer le repertoire $NewWorkDir",LogFile)
	System.exit(1)
} else {
	logger("INFO","creation de $NewWorkDir",LogFile)
}
System.exit(0)

// ###################################### FIN CREATE WORKSPACE