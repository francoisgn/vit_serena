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
 def String SBM_Environnement="Production"
 def String SBM_Depot="C:\\Temp\\Sources"
 def String SBM_Projet="Projet"
 def String SBM_BaselineId="BaselineId"
 def String SBM_Nom_Log="SBM_Nom_Log"
 def String SBM_Folder="Folder"
 def String SBM_ORA_Login="ORA_Login"
 def String SBM_ORA_Password="ORA_Password"
 def String SBM_ORA_SID="ORA_SID"
 def String SBM_Script="Script"
 def String SBM_ORA_Base="MY_ORA_BASE"
 def String SBM_ORA_Home="/products/oracle"
 def String SBM_ORA_Path="C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files (x86)\\QuickTime\\QTSystem\\"
 def String SBM_ORA_Lib="PATH_LD_LIBRARY_PATH"
 
 /* decommenter pour chargement common_lib dans SRA
 ${p:lib_groovy}
 
 
 
 // Variables SBM
 def String SBM_Environnement="${p:SBM_Environnement}"
 def String SBM_Depot="${p:SBM_Depot}"
 def String SBM_Projet="${p:SBM_Projet}"
 def String SBM_BaselineId="${p:SBM_BaselineId}"
 def String SBM_Nom_Log="${p:SBM_Nom_Log}"
 def String SBM_Folder="${p:SBM_Folder}"
 def String SBM_ORA_Login="${p:SBM_ORA_Login}"
 def String SBM_ORA_Password="${p:SBM_ORA_Password}"
 def String SBM_ORA_SID="${p:SBM_ORA_SID}"
 def String SBM_Script="${p:SBM_Script}"
 def String SBM_ORA_Base="${p:SBM_ORA_Base}"
 def String SBM_ORA_Home="${p:SBM_ORA_Home}"
 def String SBM_ORA_Path="${p:SBM_ORA_Path}"
 def String SBM_ORA_Lib="${p:SBM_ORA_Lib}"
 
 
  */
 
 
 // workDir
 def String workDir="${SBM_Depot}"+File.separator+"${SBM_Projet}"+File.separator+"${SBM_BaselineId}"
 
 // variables input SQL file
 def String varL_SQLFiles="REWIND.sql:rollback"
 def String sqlscriptname=""
 
 // Logfile
 def String SqlLogDir=workDir+File.separator+"${SBM_Folder}"+File.separator+"log"
 def String LogFile=workDir+File.separator+"log"+File.separator+"${SBM_Nom_Log}"
 
 
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
 logger("INFO","lancement de la phase de rollback",LogFile)
 
 
 

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
		 sqlscript=new File(workDir+File.separator+"${SBM_Folder}"+File.separator+File.separator+subdir+File.separator+sqlline)
 sqlscriptname=sqlscript.getName()
 logger("TRACE","nom du script "+sqlscriptname)
 reslogFile=new File(SqlLogDir+File.separator+sqlscriptname+".res")
 errlogFile=new File(SqlLogDir+File.separator+sqlscriptname+".err")
 logger("INFO","execution de "+sqlscriptname,LogFile)
 
 try{
	 
	 def command = "${SBM_Script};sqlplus ${SBM_ORA_Login}/${SBM_ORA_Password}@${SBM_ORA_SID} @${sqlscript} 1>> ${reslogFile} 2> ${errlogFile}"
	 logger("TRACE","cmd: "+command)
	 // --- a supprimer pour execution
	 command="echo coucou"
	 reslogFile << 'SP2-9999'
	 // --- a supprimer pour execution
	 if(execute_oracle_sql(envp,command)!=0){
		 logger("ERROR","Probleme lors de l'execution de la commande  $command ",LogFile)
		 System.exit(1)
	 } else {
	 logger("TRACE","commande ok")
	 }
 }
 catch (Exception e){
	 logger("ERROR",e.getMessage(),LogFile)
			 System.exit(1)
		 }
 
		 logger("INFO","verification des logs "+reslogFile,LogFile)
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
	 logger("INFO","analyse de  "+reslogFile+" terminée - pas de warning detecté",LogFile)
	 
 } else {
	 logger("WARN","analyse de  "+reslogFile+" terminée - warning(s) detecté(s)",LogFile)
	 if(oraerrorlog==true){
		 logger("WARN","erreur ORA-xxxxx dans la log : "+reslogFile,LogFile)
	 }
	 if(sperrorlog==true){
		 logger("WARN","erreur SP2-xxxx dans la log : "+reslogFile,LogFile)
	 }
	 if(prompterrorlog==true){
		 logger("WARN","absence de prompt SQL trouvé dans la log : "+reslogFile,LogFile)
			 }
		 }
	 }
 }
 System.exit(0)
 


