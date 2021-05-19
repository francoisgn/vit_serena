
// ------------------------- fonction_logger

def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		def File outputFile = new File(outputFileStr)
		outputFile<< "$timpeStamp:[$level] $message\n"
	}
}
// ------------------------- fonction_execute
def int executeCmd(def envp=[],def String command){
	def cr=0
	try{
		logger("INFO","call execution")
		logger("DEBUG","command : $command")
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
		logger("ERROR","Fail to execute : cmd = $command\n Exception occured \n ${e.message}")
	}
	return cr
}
// Calcul des var environnement
def Map env=System.getenv()
def envp=[]
env.each{ key,val->
		envp.add("$key=${val}")
	}
//	envp.add("PATH=SYBASEPATH")


/**
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
*/



// ------------------------- fonction_execute : appel



// ------------------- APPEL TransfertWBck



def String fileSep=File.separator
def String LogFile="C:${fileSep}TestT2N${fileSep}Log.log"
def String sourcepath="C:${fileSep}TestT2N${fileSep}Source${fileSep}"
def String targetpath="C:${fileSep}TestT2N${fileSep}Target${fileSep}"
def String backuppath
//def String backuppath="C:${fileSep}TestT2N${fileSep}Backup${fileSep}"

try{
	def command = "blablablabla commande"// Create the String
	def value=executeCmd(envp,command)
	if(value!=0){
		throw new Exception("Erreur dans l'execution")
	}
}
catch (Exception e){
	System.exit(1)
}

// APPEL
