

/*****
* ---------------------------------------------------------------------------------------------------------------------------------------------------
* ----------------------------------------------------LIB DOLLARU------------------------------------------------------------------------------------
* ---------------------------------------------------------------------------------------------------------------------------------------------------
*/
// lib_delete_obj
def int delete_obj(def String obj,def String workBaseDir, def String envScript,def String espace, def String node,def String LogFile){
	//$DU_Script_on_remote${DU_env_s}UXEXE${DU_env_e}${DU_slash_env}uxext upr $DU_Espace NODE=$Noeud upr=$DU_Object output="$DU_Object" >> $Env_Path_Current/log/$Nom_Log
	def String objType=obj.replaceAll("/.*\$","").toLowerCase()
	def String objName=obj.replaceAll("^.*/","")
	
	
	
	
	def exportCommand=""
	if(objType.equals("upr")){
		exportCommand="uxdlt upr ${espace} node=${node} upr=\"$objName\""
	}
	else if(objType.equals("ses")){
		exportCommand="uxdlt ses ${espace} node=${node} ses=\"$objName\""
	}
	else if(objType.equals("rul")){
		exportCommand="uxdlt rul ${espace} node=${node} rul=\"$objName\""
	}
	else if(objType.equals("class")){
		exportCommand="uxdlt adm  node=${node} classe=\"$objName\""
	}
	else if(objType.equals("res")){
		exportCommand="uxdlt res ${espace} node=${node} res=\"$objName\""
	}
	else if(objType.equals("appl")){
		exportCommand="uxdlt appl ${espace} node=${node} appl=\"$objName\""
		
		
	}
	else if(objType.equals("tsk")){
		def tabObjectName=objName.split("#")
		if(tabObjectName.size()!= 3){
			logger("ERROR","Le nom de l'objet ${obj} est invalide: une task doit etre de la forme tsk/uproc#ses#mu",LogFile)
			return 1
		}
		def uprocName=tabObjectName[0]
		def sessionName=tabObjectName[1]
		def muName=tabObjectName[2]
		
		exportCommand="uxdlt tsk ${espace} node=${node} upr=\"$uprocName\" ses=\"$sessionName\" mu=\"$muName\""
	}
	else{
		logger("ERROR","Le type d'obj ${objType} n'est pas pris en charge",LogFile)
		return 1
	}
	
	// lancement de la suppression Dollar U
	def Map result=wrapper_dollarU_cmdline(workBaseDir,envScript,exportCommand)
	
	if(result["exitcode"]!=0){
		logger("ERROR","Suppression de l'objet ${obj} : KO \n"+result["stdout"]+"\n"+result["stderr"],LogFile)
		return 1
	}
	else{
		logger("INFO","Suppression de l'objet ${obj} : OK",LogFile)
	}

	return 0
}
/*****
* ---------------------------------------------------------------------------------------------------------------------------------------------------
*/
// lib_export_obj

def int export_obj(def String obj,def String exportBaseDir,def String workBaseDir, def String envScript,def String espace, def String node,def String LogFile){
	//$DU_Script_on_remote${DU_env_s}UXEXE${DU_env_e}${DU_slash_env}uxext upr $DU_Espace NODE=$Noeud upr=$DU_Object output="$DU_Object" >> $Env_Path_Current/log/$Nom_Log
	def String objType=obj.replaceAll("/.*\$","").toLowerCase()
	def String objName=obj.replaceAll("^.*/","")
	logger("DEBUG","objType=$objType",LogFile)
	logger("DEBUG","objName=$objName",LogFile)
	
	def File exportDir=new File(exportBaseDir+File.separator+objType)
	if(!exportDir.isDirectory()){
		if(!exportDir.mkdirs()){
			logger("ERROR","Impossible de creer le repertoire "+exportDir.getAbsolutePath(),LogFile)
			return 1
		}
	}
	
	def exportCommand=""
	if(objType.equals("upr")){
		exportCommand="uxext upr ${espace} node=${node} upr=\"$objName\" output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("ses")){
		exportCommand="uxext ses ${espace} node=${node} ses=\"$objName\" output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("rul")){
		exportCommand="uxext rul ${espace} node=${node} rul=\"$objName\" output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("class")){
		exportCommand="uxext adm ${espace} node=${node} classe=\"$objName\" output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("res")){
		exportCommand="uxext res ${espace} node=${node} res=\"$objName\" output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("appl")){
		exportCommand="uxext appl ${espace} node=${node} output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else if(objType.equals("tsk")){
		def tabObjectName=objName.split("#")
		if(tabObjectName.size()!= 3){
			logger("ERROR","Le nom de l'objet ${obj} est invalide: une task doit etre de la forme tsk/uproc#ses#mu",LogFile)
			return 1
		}
		def uprocName=tabObjectName[0]
		def sessionName=tabObjectName[1]
		def muName=tabObjectName[2]
		
		exportCommand="uxext tsk ${espace} node=${node} upr=\"$uprocName\" ses=\"$sessionName\" mu=\"$muName\" repl output=\""+exportDir.getCanonicalPath()+File.separator+objName+"\""
	}
	else{
		logger("ERROR","Le type d'obj ${objType} n'est pas pris en charge",LogFile)
		return 1
	}
	
	// lancement de l'export Dollar U
	def Map result=wrapper_dollarU_cmdline(workBaseDir,envScript,exportCommand)
	
	if(result["exitcode"]!=0){
		logger("ERROR","export de l'objet ${obj} : KO \n"+result["stdout"]+"\n"+result["stderr"],LogFile)
		return 1
	}
	else{
		logger("INFO","export de l'objet ${obj} : OK",LogFile)
	}
	/***
	 * result["exitcode"]=1
	result["stderr"]=""
	result["stdout"]="Unknown"
		 def String Dollar_U_Espace="X"
		def String Noeud="GFHXRTCMP1"
	****/
	return 0
}
/*****
* ---------------------------------------------------------------------------------------------------------------------------------------------------
*/
// lib_import_obj

def int import_obj(def String obj,def String importBaseDir,def String workBaseDir, def String envScript,def String espace, def String node,def String LogFile){
	//$DU_Script_on_remote${DU_env_s}UXEXE${DU_env_e}${DU_slash_env}uxext upr $DU_Espace NODE=$Noeud upr=$DU_Object output="$DU_Object" >> $Env_Path_Current/log/$Nom_Log
	def String objType=obj.replaceAll("/.*\$","").toLowerCase()
	def String objName=obj.replaceAll("^.*/","")
	logger("DEBUG","objType=$objType",LogFile)
	logger("DEBUG","objName=$objName",LogFile)
	
	def File importDir=new File(importBaseDir+File.separator+objType)
	
	
	def exportCommand=""
	if(objType.equals("upr")){
		exportCommand="uxins upr ${espace} node=${node} upr=\"$objName\" input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else if(objType.equals("ses")){
		exportCommand="uxins ses ${espace} node=${node} ses=\"$objName\" input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else if(objType.equals("rul")){
		exportCommand="uxins rul ${espace} node=${node} rul=\"$objName\" input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else if(objType.equals("class")){
		exportCommand="uxins adm  node=${node} classe=\"$objName\" input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else if(objType.equals("res")){
		exportCommand="uxins res ${espace} node=${node} res=\"$objName\" input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else if(objType.equals("appl")){
		logger("WARNING","Import APPL demande mais non implemente !",LogFile)
		return 0
		
	}
	else if(objType.equals("tsk")){
		def tabObjectName=objName.split("#")
		if(tabObjectName.size()!= 3){
			logger("ERROR","Le nom de l'objet ${obj} est invalide: une task doit etre de la forme tsk/uproc#ses#mu",LogFile)
			return 1
		}
		def uprocName=tabObjectName[0]
		def sessionName=tabObjectName[1]
		def muName=tabObjectName[2]
		
		exportCommand="uxins tsk ${espace} node=${node} upr=\"$uprocName\" ses=\"$sessionName\" cible=\"$muName\"  input=\""+importDir.getCanonicalPath()+File.separator+objName+"\" repl"
	}
	else{
		logger("ERROR","Le type d'obj ${objType} n'est pas pris en charge",LogFile)
		return 1
	}
	
	// lancement de l'import Dollar U
	def Map result=wrapper_dollarU_cmdline(workBaseDir,envScript,exportCommand)
	
	if(result["exitcode"]!=0){
		logger("ERROR","import de l'objet ${obj} : KO \n"+result["stdout"]+"\n"+result["stderr"],LogFile)
		return 1
	}
	else{
		logger("INFO","import de l'objet ${obj} : OK",LogFile)
	}

	return 0
}
/*****
* ---------------------------------------------------------------------------------------------------------------------------------------------------
*/
// lib_isExist_obj

def Boolean isExist_obj(def String obj,def String workBaseDir, def String envScript,def String espace, def String node,def String LogFile){
	//$DU_Script_on_remote${DU_env_s}UXEXE${DU_env_e}${DU_slash_env}uxext upr $DU_Espace NODE=$Noeud upr=$DU_Object output="$DU_Object" >> $Env_Path_Current/log/$Nom_Log
	def String objType=obj.replaceAll("/.*\$","").toLowerCase()
	def String objName=obj.replaceAll("^.*/","")
	
	
	
	
	def exportCommand=""
	if(objType.equals("upr")){
		exportCommand="uxshw upr ${espace} node=${node} upr=\"$objName\""
	}
	else if(objType.equals("ses")){
		exportCommand="uxshw ses ${espace} node=${node} ses=\"$objName\""
	}
	else if(objType.equals("rul")){
		exportCommand="uxshw rul ${espace} node=${node} rul=\"$objName\""
	}
	else if(objType.equals("class")){
		exportCommand="uxshw adm  node=${node} classe=\"$objName\""
	}
	else if(objType.equals("res")){
		exportCommand="uxshw res ${espace} node=${node} res=\"$objName\""
	}
	else if(objType.equals("appl")){
		exportCommand="uxshw appl ${espace} node=${node} appl=\"$objName\""
		
		
	}
	else if(objType.equals("tsk")){
		def tabObjectName=objName.split("#")
		if(tabObjectName.size()!= 3){
			logger("ERROR","Le nom de l'objet ${obj} est invalide: une task doit etre de la forme tsk/uproc#ses#mu",LogFile)
			return false
		}
		def uprocName=tabObjectName[0]
		def sessionName=tabObjectName[1]
		def muName=tabObjectName[2]
		
		exportCommand="uxshw tsk ${espace} node=${node} upr=\"$uprocName\" ses=\"$sessionName\" mu=\"$muName\""
	}
	else{
		logger("ERROR","Le type d'obj ${objType} n'est pas pris en charge",LogFile)
		return false
	}
	
	// execution Dollar U
	def Map result=wrapper_dollarU_cmdline(workBaseDir,envScript,exportCommand)
	
	if(result["exitcode"]!=0){
		return false
	}
	else{
		return true
	}
}
/*****
* ---------------------------------------------------------------------------------------------------------------------------------------------------
*/
// lib_wrapper_dollarU_cmdline

def wrapper_dollarU_cmdline(def String workdir, def String envScript,def String cmdline){
	def File tempFile
	
	
	def Map result=[:]
	result["exitcode"]=1
	result["stderr"]=""
	result["stdout"]="Unknown"
	def command=""
	
	
	if (System.properties['os.name'].toLowerCase().contains('windows')) {
		tempFile=new File(workdir+File.separator+"myshell.cmd")
	}
	else{
		tempFile=new File(workdir+File.separator+"myshell.sh")
	}
	
	if(tempFile.exists()){
		if(! tempFile.delete()){
			result["exitcode"]=1
			result["stderr"]=""
			result["stdout"]="Impossible de supprimer "+tempFile.getAbsolutePath()
			return result
		}
	}
	
	if (System.properties['os.name'].toLowerCase().contains('windows')) {
		def output = tempFile.newDataOutputStream()
		output << "@echo off\r\n"
		output << "call ${envScript} > nul\r\n"
		output << "%UXEXE%\\${cmdline}\r\n"
		output << "exit /b %ERRORLEVEL%\r\n"
		output.close()
		
		command="cmd /c "+tempFile.getAbsolutePath()
		
		
	}
	else{
		def output = tempFile.newDataOutputStream()
		output << ". ${envScript} > /dev/null\n"
		output << "\${UXEXE}/${cmdline}\n"
		output << "exit \$?\n"
		output.close()
		
		command="sh "+tempFile.getAbsolutePath()
		
	}
	
	try{
		
		
		StringBuffer sbout = new StringBuffer()
		StringBuffer sberr = new StringBuffer()
		//println "command :$command"
		def process = command.execute() // Call *execute* on the string
		def tout = process.consumeProcessOutputStream(sbout)
		def terr = process.consumeProcessErrorStream(sberr)
		
		 process.waitFor()                               // Wait for the command to finish
	   // Obtain status and output
		 def value = process.exitValue()
		 result["exitcode"]=process.exitValue()
		 result["stderr"]=sberr.toString()
		 result["stdout"]=sbout.toString()
		 
		 
	}
	catch (Exception e){
		result["exitcode"]=1
		result["stderr"]=""
		result["stdout"]=e.getMessage()
	}
	finally{
		//tempFile.delete()
		return result
	}
	return result
}