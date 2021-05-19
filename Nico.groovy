
def String var="ddd"

println "dfdde ${var} "+var

def File d=new File("c:\\nicodir")
d.eachDirRecurse { it->
}
d.eachFile { it->
}
d.eachFileRecurse  { it->
}


def File f=new File("c:\\nico.txt")
def String fileContent=f.text

if(fileContent== ""){ println "file size == null";}

def Boolean findit=false
try{
	fileContent.eachLine { it->
		def String line=it.trim()
		if(line =~ "^toto"){ 
			findit=true;
			println "toto find"
			throw new Exception("findit")
		}
	}
}
catch(Exception e){
	// element trouve
	println "erreor message="+e.getMessage()
	findit=true;
}
for(def int i=0;i<100;i++){
	println "i=$i"
	if(i==50){ break}
}

def tab=[]
tab.add("aaa")
tab.add("bbb")
tab.each { it->
	
}
def map=[:]
map["token1"]="val1"
map["token2"]="val2"
map.each{  token, val->

}
def String s="MAIN.sql:release,VALIDATE.sql:validate,REWIND.sql:rollback"
def tab1=s.split(",")
tab1.each{
	def tab2=it.split(":")
	def file=tab2[0].trim()
	def subdir=tab2[1].trim()
	
}

def String s1="MAIN.sql,VALIDATE.sql,REWIND.sql"
def String s2="release,validate,rollback"
def s1tab=s1.split (",")
def s2tab=s2.split (",")
for (def int j=0;j<s1tab.size() ;j++){
	def file=s1tab[j].trim()
	def subdir=s2tab[j].trim()
	
}


try{
	def command = """cmd /c ${CST_DSTAGE_IsTool_Path} -script ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_${targetServerInst}.txt"""// Create the String
			 // !!!!!!!!!!!!!!!!!!
			 // a supprimer pour activer l'export
			 command="""cmd /c echo ${command}"""
	// !!!!!!!!!!!!!!!!!!
	logger("INFO","Export Partiel des ISX","$VAR_DSTAGE_LogFileName")
	logger("INFO","cmd:${command}","$VAR_DSTAGE_LogFileName")
	
	StringBuffer sbout = new StringBuffer()
	StringBuffer sberr = new StringBuffer()
	// lancement de la commandline dans un thread en background
	def process = command.execute(null, new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}")) // Call *execute* on the string
	// Rq: il est preferable d'utiliser des StringBuffer pour ne pas saturer les buffers de sortie du thread en chrage de la cmmande
	// et d'appeler la method consumeProcessOutputStream et consumeProcessErrorStream
	def tout = process.consumeProcessOutputStream(sbout)
	def terr = process.consumeProcessErrorStream(sberr)
	// attente de termainison du thread
	 process.waitFor()                               // Wait for the command to finish
   // Obtain status and output
	 def value = process.exitValue()

	 logger("TRACE","return code: ${value}","$VAR_DSTAGE_LogFileName")
	 if(value==0){
		  logger("TRACE",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
		  logger("TRACE",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
		  logger("INF0","backup partiel sur $targetServerInst reussie","$VAR_DSTAGE_LogFileName")
	 }
	 else{
		 
		 logger("ERROR",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
		 logger("ERROR",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
		 logger("ERROR","echec du backup partiel sur $targetServerInst","$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }
}
catch (Exception e){
	logger("ERROR",e.getMessage(),"$VAR_DSTAGE_LogFileName")
	System.exit(1)
		 }


def File tempFile=new File(workdir+File.separator+"myshell.cmd")
def output = tempFile.newDataOutputStream()
output << "@echo off\r\n"
output << "call ${envScript} > nul\r\n"
output << "%UXEXE%\\${cmdline}\r\n"
output << "exit /b %ERRORLEVEL%\r\n"
output.close()


def String toto

println toto

def String tata=""
println tata

if(toto == "aaa"){
}
if("aaa".equals(toto)){
}

}