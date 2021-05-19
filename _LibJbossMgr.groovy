
// ------------------------- fonction_logger

def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		def File outputFile = new File(outputFileStr)
		outputFile<< "$timpeStamp:[$level] $message\n"
	}
}
// ------------------------- fonction_JbossMgr
import org.jboss.as.cli.scriptsupport.*

//def int JbossMrg(def String clitarget,def String clicmd){


	cli = CLI.newInstance()
	cli.connect()
	
	// context standalone ou domain
	if (cli.getCommandContext().isDomainMode()) {
	  cli.cmd("cd /host=master/core-service=platform-mbean/type=runtime")
	} else {
	  cli.cmd("cd /core-service=platform-mbean/type=runtime")
	}
	  
	result = cli.cmd(":read-attribute(name=start-time)")
	response = result.getResponse()
	startTime = response.get("result").asLong()
	  
	result = cli.cmd(":read-attribute(name=uptime)")
	response = result.getResponse()
	serveruptime = response.get("result").asString()
	  
	println()
	println("The server was started on " + new Date(startTime))
	println("It has been running for " + serveruptime + "ms")
	  
	cli.disconnect()
	
//}