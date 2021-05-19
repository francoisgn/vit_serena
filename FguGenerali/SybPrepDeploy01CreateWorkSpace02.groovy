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


def String SBM_Environnement="Recette"
def String SBM_Depot="C:\\Temp\\TestSybase"
def String SBM_Projet="ProjetSYB"
def String SBM_BaselineId="SBM_BaselineId"
def String SBM_Nom_Log="SBM_NomLog"
def String SBM_Folder="SBM_Folder"



/* decommenter pour chargement common_lib dans SRA
 ${p:lib_groovy}
 
def String SBM_Environnement="${p:SBM_Environnement}"
def String SBM_Depot="${p:SBM_Depot}"
def String SBM_Projet="${p:SBM_Projet}"
def String SBM_BaselineId="${p:SBM_BaselineId}"
def String SBM_Nom_Log="${p:SBM_Nom_Log}"
def String SBM_Folder="${p:SBM_Folder}"

*/






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


// Log
def String LogFile="${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
def String SqlLogDir=workDir+File.separator+"${SBM_Folder}"+File.separator+"log"

logger("TRACE","Debut du prepdeploy")
logger("TRACE","Creation des workspace ")

// Purge ${SBM_Depot}/${sybEnv}/${SBM_Projet}
def File OldWorkDir
OldWorkDir=new File("${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}")
logger("TRACE","suppression de $OldWorkDir")
if(OldWorkDir.exists()){
	if(! OldWorkDir.deleteDir()){
		logger("ERROR","Impossible de supprimer "+OldWorkDir.getAbsolutePath())
		System.exit(1) 
	}
}

// --- pourquoi creer le subdir $Projet le travail est effectue dans le subdir $Folder
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
	logger("ERROR","Impossible de creer le repertoire $LogDir")
	System.exit(1)
}
logger("TRACE","------------------------------------- ")
//outProps.setProperty("Var_Exp_workDir","${workDir}")

System.exit(0)