
// ------------------------- fonction_logger

def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		def File outputFile = new File(outputFileStr)
		outputFile<< "$timpeStamp:[$level] $message\n"
	}
}

//	-------------------------------------------------------------------------------
//	lib transfert to nas
//def String fileSep=File.separator
def int TransfertWBck(def String sourceTarget,def String destTarget,def String bckTarget=null){
	logger("INFO","---------- check input path (source, destination, backup)")
	if(new File(sourceTarget).isDirectory()){
		if(!new File(destTarget).isDirectory()){
			logger("WARN","${destTarget} non-existent - creation")
			if(!new File("${destTarget}").mkdirs()){
				logger("ERROR","unable to create ${destTarget}")
				return 1
			}	
		} else {
			if(bckTarget != null){
				if(!new File(bckTarget).isDirectory()){
					logger("WARN","${bckTarget} non-existent - creation")
					if(!new File("${bckTarget}").mkdirs()){
						logger("ERROR","unable to create ${bckTarget}")
						return 1
					}
				}
			} else {
				logger("WARN","bckTarget is null - running without backup")
			}
		}
	} else {
		logger("ERROR","${sourceTarget} n'existe pas")
		System.exit(1)
	}
	if(bckTarget != null){
		logger("INFO","---------- backup destination's files that will be overwritten")
		new File(sourceTarget).eachFileRecurse() { file ->
			def String sourcefile="${file}"
			if(new File(sourcefile).isFile()){	
				def String sourcerelative=sourcefile.minus(sourceTarget)
				def String destfilepath="${destTarget}"+file.separator+"${sourcerelative}"
				if(new File(destfilepath).isFile()){
					logger("DEBUG","source and destination file found : ${sourcerelative} - backup")
					def String backfilepath="${bckTarget}"+file.separator+"${sourcerelative}"
					if(new File(backfilepath).isFile()){
						logger("DEBUG","backup file exists : ${backfilepath} - overwriting")
					}
					def ant = new AntBuilder()
					try {
							ant.copy(verbose: "true",file:"${sourcefile}", tofile: "${backfilepath}", overwrite:'true',includeEmptyDirs:'true') {
							}
					}
					catch (Exception e) {
						logger("ERROR","error writing ${backfilepath} \n ${e.message}")
						logger("ERROR","exit - backup have not been completed")
						System.exit(1)
					}
					logger("DEBUG","backed-up : ${backfilepath}")
				}
			}	
		}
	logger("INFO","---------- backup destination's files that will be overwritten --- successfully ended")
	}
	logger("INFO","---------- data deployment to ${destTarget}")
	def ant = new AntBuilder()
	try {
			ant.copy(verbose: "true",  todir: "${destTarget}", overwrite:'true',includeEmptyDirs:'true') {
				fileset(dir: "${sourceTarget}"){ include(name:"**//*") }
			}
	}
	catch (Exception e) {
		logger("ERROR","error writing ${sourceTarget} to ${destTarget} \n ${e.message}")
		logger("ERROR","exit - data deployment failed")
		System.exit(1)
	}
	logger("INFO","---------- data deployment to ${destTarget} --- successfully ended")
	return 0
}


// ------------------- APPEL TransfertWBck



def String fileSep=File.separator
def String LogFile="C:${fileSep}TestT2N${fileSep}Log.log"
def String sourcepath="C:${fileSep}TestT2N${fileSep}Source${fileSep}"
def String targetpath="C:${fileSep}TestT2N${fileSep}Target${fileSep}"
def String backuppath
//def String backuppath="C:${fileSep}TestT2N${fileSep}Backup${fileSep}"

TransfertWBck(sourcepath,targetpath,backuppath)
