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
def String SBM_SYB_DS="SBM_SYB_DS"
def String SBM_SYB_DB="SBM_SYB_DB"
def String SBM_SYB_Login="SBM_SYB_Login"
def String SBM_Script="SBM_Script"
def String SBM_SYB_Password="SBM_SYB_Password"
def String SBM_SYB_Forced="Oui"



/* decommenter pour chargement common_lib dans SRA
 ${p:lib_groovy}
 
def String SBM_Environnement="${p:SBM_Environnement}"
def String SBM_Depot="${p:SBM_Depot}"
def String SBM_Projet="${p:SBM_Projet}"
def String SBM_BaselineId="${p:SBM_BaselineId}"
def String SBM_Nom_Log="${p:SBM_Nom_Log}"
def String SBM_Folder="${p:SBM_Folder}"
def String SBM_SYB_DS="${p:SBM_SYB_DS}"
def String SBM_SYB_DB="${p:SBM_SYB_DB}"
def String SBM_SYB_Login="${p:SBM_SYB_Login}"
def String SBM_Script="${p:SBM_Script}"
def String SBM_SYB_Password="${p:SBM_SYB_Password}"
def String SBM_SYB_Forced="${p:SBM_SYB_Forced}"
*/

logger("INFO","debut de la phase de deploiement")

// variables input SQL related
def String varL_SQLFiles="MAIN.sql:release,VALIDATE.sql:validate"

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


// Logfile - purge et creation log vide
def String LogFile="${SBM_Depot}"+File.separator+"${sybEnv}"+File.separator+"${SBM_Projet}"+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
def File OldLog=new File(LogFile)
if(OldLog.exists()){
	logger("TRACE","le fichier existe "+OldLog.getAbsolutePath())
	if(! OldLog.delete()){
		logger("ERROR","Impossible de supprimer "+OldLog.getAbsolutePath())
		System.exit(1)
	}
} else {
def File NewLog=new File(LogFile)
}


def String s=varL_SQLFiles
def tab1=s.split(",")
tab1.each{
	def tab2=it.split(":")
	def file=tab2[0].trim()
	def subdir=tab2[1].trim()
	sqlfile=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+file)
	sqlsubdir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+subdir)

	def String sqlfileContent=sqlfile.text
	sqlfileContent.eachLine { itsql->
		def String sqlline=itsql.trim()
		sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+subdir+File.separator+sqlline)
		def File sqllogDir=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+"log")
		if(!sqllogDir.exists()){
			if(!sqllogDir.mkdirs()){
				logger("ERROR","Impossible de creer le repertoire $sqllogDir",LogFile)
				System.exit(1)
			}
		}
		reslogFile=new File("${sqllogDir}"+File.separator+sqlline+".res")
		// --- Map environnement ORACLE a adapter pour ISQL
		// Calcul des var environnement
		def Map env=System.getenv()
		def envp=[]
		env.each{ key,val->
				envp.add("$key=${val}")
			}
		//	envp.add("PATH=SYBASEPATH")
		
		logger("INFO","execution de "+sqlscript,LogFile)
		try{
			def command = "${SBM_Script}; isql -S${SBM_SYB_DS}  -D${SBM_SYB_DB}  -U${SBM_SYB_Login}  -P${SBM_SYB_Password}  -i ${sqlscript}  -w 999999 -o ${reslogFile}"// Create the String
			// --- a supprimer pour execution
			logger("TRACE","cmd: "+command)
			command="echo coucou"
			reslogFile << 'SUCCESS'
			// --- a supprimer pour execution
			def value=execute_oracle_sql(envp,command)
			logger("TRACE","value=$value")
			logger("TRACE","return code: ${value}")
			if(value!=0){
				throw new Exception("Erreur dans l'execution de "+sqlscript)
			}
		
		}
		catch (Exception e){
			logger("ERROR",e.getMessage(),LogFile)
			System.exit(1)
		}
		logger("INFO","controle du fichier journal "+reslogFile,LogFile)
		def String logContent=reslogFile.text
		def Boolean syberror=false
		try{
			logContent.eachLine { logit->
				if(syberror==false){
					if(logit.matches("(?i).*FAILED.*")){
						syberror=true
						throw new Exception("FAILED Aparait dans le log")
					}
				}
			}
		}
		catch(Exception e){
			println "WARNING : "+e.getMessage()
		}
		
		// VERIFIER QUE LE SYB_FORCED OBLIGE BIEN LA SORTIE EN CR=0 MALGRES LE FAILED
		if(syberror==true){
			logger("WARNING","erreur FAILED detecté dans le fichier de journal",LogFile)
			if (SBM_SYB_Forced=="Oui"){
				logger("TRACE","SYB_Forced = "+SBM_SYB_Forced)
				logger("TRACE","On force la sortie en CR=0")
			} else { 
				logger("TRACE","SYB_Forced = "+SBM_SYB_Forced)
				logger("TRACE","On force la sortie en CR=1")
				System.exit(1)
			}			
		} else {
			logger("INFO","Pas d'erreur dans le fichier de journal",LogFile)
		}
	}
}
logger("INFO","Fin du Deploy",LogFile)
System.exit(0)

