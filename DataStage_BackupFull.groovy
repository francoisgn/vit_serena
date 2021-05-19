//-------------------------------------------------------------------------------- InitStep
//chargement des libs
def logger (def String level,def String message,def String outputFileStr=null) {
	def timpeStamp=new Date().format("yyyyMMdd-kkmmss")
	println "$timpeStamp:[$level] $message"
	if (outputFileStr!=null && outputFileStr !="" && new File("$outputFileStr").getParentFile().isDirectory()){
		def File outputFile = new File(outputFileStr)
		outputFile<< "$timpeStamp:[$level] $message\n"
	}
}

// pas de $LogFileName - le fichier peut ne pas exister
fileSep=File.separator

// input SBM
logger("TRACE","chargement des inputs SBM")
SBM_BaselineId="BL_PROJET_INITIAL_30.10.2013_15.21.51_9470"
SBM_Package="Projet_Initial_WAP-6037_0.zip"
SBM_Folder="DATASTAGE"
SBM_Nom_Log="BL_PROJET_INITIAL_30.10.2013_15.21.51_9470.txt"
SBM_Projet="Projet_Initial"
SBM_BckPackage="BCK_PROJET_INITIAL_30.10.2013_15.21.51_9470.zip"
SBM_Password="mypassword"
SBM_Login="MyLogin"
SBM_Target_Server="Server1"
SBM_Target_Server2="Server2"
SBM_Target_Server3="Server3"
SBM_Environnement="Recette"


//  Env. Cst
logger("TRACE","Chargement constantes d'environnement")
VAR_CST_DSTAGE_tmp_directories="tmp|tag|bck|log|tmp/init|pkg"
VAR_CST_DSTAGE_Root_Directory="D:\\Deploy\\Serena\\"
VAR_CST_DSTAGE_ArboDisque_Files=""
VAR_CST_DSTAGE_NAS_Folders="02-NAS\\Fichiers_manuels|02-NAS\\Fichiers_sortie|02-NAS\\Fichiers_temporaires|02-NAS\\Init|02-NAS\\Sql|02-NAS\\Stockage"
VAR_CST_DSTAGE_M_Folders="05-ArboDisques-M\\Suivi\\Master|05-ArboDisques-M\\Suivi\\TR|05-ArboDisques-M\\Suivi\\Validation"
VAR_CST_DSTAGE_IsTool_Path="D:\\IS\\Clients\\istools\\cli\\istool.exe"
VAR_CST_DSTAGE_Port_Domaine="9080"
VAR_CST_DIM_SASDepot="G:\\Test\\Serena\\DIM"
VAR_CST_Nas_Projet_Path="\\\\VIT-INF-P053\\Simulation-NAS\\DS"
VAR_CST_DSTAGE_Environnement="Recette|Production|Pre-Production"


// VARIABLES
logger("TRACE","Chargement variables")
// definition des variables necessaires au calcul des OutProps
def VarL_DSTAGE_Fichiers_Manuels_Directory="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_manuels"
def VarL_DSTAGE_Fichiers_Sortie="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_sortie"
def VarL_DSTAGE_Fichiers_Temporaires="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_temporaires"
def VarL_DSTAGE_Fichiers_Stockage="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Stockage"
def VarL_DSTAGE_Fichiers_Sql="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}SQL"
def VarL_DSTAGE_Fichiers_Init="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Init"
def VarL_DSTAGE_Fichiers_Master="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Master"
def VarL_DSTAGE_Fichiers_TR="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}TR}"
def VarL_DSTAGE_Fichiers_Validation="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Validation"
def VarL_DSTAGE_Workspace="${CST_DSTAGE_Root_Directory}${fileSep}${SBM_BaselineId}"

// variable en OutProps
def VAR_DSTAGE_Fichiers_Manuels_Directory="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_manuels"
def VAR_DSTAGE_Fichiers_Sortie="${CST_Nas_Projet_Path}${SBM_Projet}${fileSep}Fichiers_sortie"
def VAR_DSTAGE_Fichiers_Temporaires="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_temporaires"
def VAR_DSTAGE_Fichiers_Stockage="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Stockage"
def VAR_DSTAGE_Fichiers_Sql="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}SQL"
def VAR_DSTAGE_Fichiers_Init="${CST_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Init"
def VAR_DSTAGE_Fichiers_Hashcode1="DS${fileSep}${SBM_Projet}${fileSep}fichiers_hashcodes"
def VAR_DSTAGE_Fichiers_Instance_Temporaires1="DS${fileSep}${SBM_Projet}${fileSep}fichiers_temporaires"
def VAR_DSTAGE_Compte_Rendu1="Masters${fileSep}${SBM_Projet}${fileSep}Compte Rendu"
def VAR_DSTAGE_Master1="Masters${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Master"
def VAR_DSTAGE_TR="Masters${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}TR"
def VAR_DSTAGE_Fichiers_Validation1="Masters${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Validation"
def VAR_DSTAGE_backupfull_file="backupfull_${SBM_BckPackage}.txt"
def VAR_DSTAGE_Nas_Target_Folder="${VarL_DSTAGE_Fichiers_Manuels_Directory}|${VarL_DSTAGE_Fichiers_Sortie}|${VarL_DSTAGE_Fichiers_Temporaires}|${VarL_DSTAGE_Fichiers_Init}|${VarL_DSTAGE_Fichiers_Sql}|${VarL_DSTAGE_Fichiers_Stockage}"
def VAR_DSTAGE_Fichiers_Master="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Master"
def VAR_DSTAGE_Fichiers_TR="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}TR"
def VAR_DSTAGE_Fichiers_Validation="M:${fileSep}MASTERS${fileSep}${SBM_Projet}${fileSep}Suivi${fileSep}Validation"
def VAR_DSTAGE_M_Target_Folder="${VarL_DSTAGE_Fichiers_Master}|${VarL_DSTAGE_Fichiers_TR}|${VarL_DSTAGE_Fichiers_Validation}"
def VAR_DSTAGE_User="${SBM_Login}"
def VAR_DSTAGE_Password="${SBM_Password}"
def VAR_DSTAGE_Workspace="${CST_DSTAGE_Root_Directory}${fileSep}${SBM_BaselineId}"
def VAR_DSTAGE_LogFileName="${VarL_DSTAGE_Workspace}${fileSep}log${fileSep}${SBM_Nom_Log}"

// liste des serveurs cibles
// Combien de SBM_Target_Server necessaire?
logger("INFO","Calcul dynamique de la liste des Targets serveurs")
def VarL_DSTAGE_TargetServerTab=[]
if("${SBM_Target_Server}" != ""){VarL_DSTAGE_TargetServerTab.add("${SBM_Target_Server}".trim())
	logger("INFO","Targets serveur:${SBM_Target_Server} ")
}
if("${SBM_Target_Server2}" != ""){VarL_DSTAGE_TargetServerTab.add("${SBM_Target_Server2}".trim())
	logger("INFO","Targets serveur:${SBM_Target_Server2} ")
}
if("${SBM_Target_Server3}" != ""){VarL_DSTAGE_TargetServerTab.add("${SBM_Target_Server3}".trim())
	logger("INFO","Targets serveur:${SBM_Target_Server3} ")
}

def VarL_DSTAGE_TargetServerList=""
VarL_DSTAGE_TargetServerTab.each{
	if(VarL_DSTAGE_TargetServerList == ""){ VarL_DSTAGE_TargetServerList=it.trim()}else{VarL_DSTAGE_TargetServerList=VarL_DSTAGE_TargetServerList+'|'+it.trim()}
}

// out Properties target server list
def VAR_DSTAGE_TargetServerList="${VarL_DSTAGE_TargetServerList}"

//-------------------------------------------------------------------------------- InitStep
//-------------------------------------- Bloc Deploy SRA
//-------------Chargement des variables
/*
//chargement des libs
${p:lib_logger}

fileSep=File.separator
// recuperation des variables inputs SBM
def SBM_Nom_Log="${p:InitStep/Var_SBM_Nom_Log}"
def SBM_BckPackage="${p:InitStep/Var_SBM_BckPackage}"
def SBM_Projet="${p:InitStep/Var_SBM_Projet}"
def SBM_Password="${p:InitStep/Var_SBM_Password}"
def SBM_Folder="${p:InitStep/Var_SBM_Folder}"
def SBM_Package="${p:InitStep/Var_SBM_Package}"
def SBM_BaselineId="${p:InitStep/Var_SBM_BaselineId}"
def SBM_Login="${p:InitStep/Var_SBM_Login}"
def SBM_Target_Server="${p:InitStep/Var_SBM_Target_Server}"
def SBM_Target_Server2="${p:InitStep/Var_SBM_Target_Server2}"
def SBM_Target_Server3="${p:InitStep/Var_SBM_Target_Server3}"
def SBM_Environnement="${p:InitStep/Var_SBM_Environnement}"

// recuperation des constantes environnement
def VAR_CST_DSTAGE_tmp_directories="${p:InitStep/Var_CST_DSTAGE_tmp_directories}"
def VAR_CST_DSTAGE_Root_Directory="${p:InitStep/Var_CST_DSTAGE_Root_Directory}"
def VAR_CST_DSTAGE_ArboDisque_Files="${p:InitStep/Var_CST_DSTAGE_ArboDisque_Files}"
def VAR_CST_DSTAGE_NAS_Folders="${p:InitStep/Var_CST_DSTAGE_NAS_Folders}"
def VAR_CST_DSTAGE_M_Folders="${p:InitStep/Var_CST_DSTAGE_M_Folders}"
def VAR_CST_DSTAGE_IsTool_Path="${p:InitStep/Var_CST_DSTAGE_IsTool_Path}"
def VAR_CST_DSTAGE_Port_Domaine="${p:InitStep/Var_CST_DSTAGE_Port_Domaine}"
def VAR_CST_DIM_SASDepot="${p:InitStep/Var_CST_DIM_SASDepot}"
def VAR_CST_Nas_Projet_Path="${p:InitStep/Var_CST_Nas_Projet_Path}"
def VAR_CST_DSTAGE_Environnement="${p:InitStep/Var_CST_DSTAGE_Environnement}"

// recuperation des variables environnement
def VAR_DSTAGE_TargetServerList="${p:InitStep/VAR_DSTAGE_TargetServerList}"
def VAR_DSTAGE_Fichiers_Manuels_Directory="${p:InitStep/VAR_DSTAGE_Fichiers_Manuels_Directory}"
def VAR_DSTAGE_Fichiers_Sortie="${p:InitStep/VAR_DSTAGE_Fichiers_Sortie}"
def VAR_DSTAGE_Fichiers_Temporaires="${p:InitStep/VAR_DSTAGE_Fichiers_Temporaires}"
def VAR_DSTAGE_Fichiers_Stockage="${p:InitStep/VAR_DSTAGE_Fichiers_Stockage}"
def VAR_DSTAGE_Fichiers_Sql="${p:InitStep/VAR_DSTAGE_Fichiers_Sql}"
def VAR_DSTAGE_Fichiers_Init="${p:InitStep/VAR_DSTAGE_Fichiers_Init}"
def VAR_DSTAGE_Fichiers_Hashcode1="${p:InitStep/VAR_DSTAGE_Fichiers_Hashcode1}"
def VAR_DSTAGE_Fichiers_Instance_Temporaires1="${p:InitStep/VAR_DSTAGE_Fichiers_Instance_Temporaires1}"
def VAR_DSTAGE_Compte_Rendu1="${p:InitStep/VAR_DSTAGE_Compte_Rendu1}"
def VAR_DSTAGE_Master1="${p:InitStep/VAR_DSTAGE_Master1}"
def VAR_DSTAGE_TR="${p:InitStep/VAR_DSTAGE_TR}"
def VAR_DSTAGE_Fichiers_Validation1="${p:InitStep/VAR_DSTAGE_Fichiers_Validation1}"
def VAR_DSTAGE_backupfull_file="${p:InitStep/VAR_DSTAGE_backupfull_file}"
def VAR_DSTAGE_Nas_Target_Folder="${p:InitStep/VAR_DSTAGE_Nas_Target_Folder}"
def VAR_DSTAGE_Fichiers_Master="${p:InitStep/VAR_DSTAGE_Fichiers_Master}"
def VAR_DSTAGE_Fichiers_TR="${p:InitStep/VAR_DSTAGE_Fichiers_TR}"
def VAR_DSTAGE_Fichiers_Validation="${p:InitStep/VAR_DSTAGE_Fichiers_Validation}"
def VAR_DSTAGE_M_Target_Folder="${p:InitStep/VAR_DSTAGE_M_Target_Folder}"
def VAR_DSTAGE_User="${p:InitStep/VAR_DSTAGE_User}"
def VAR_DSTAGE_Password="${p:InitStep/VAR_DSTAGE_Password}"
def VAR_DSTAGE_Workspace="${p:InitStep/VAR_DSTAGE_Workspace}"
def VAR_DSTAGE_LogFileName="${p:InitStep/VAR_DSTAGE_LogFileName}"

//-------------Chargement des variables
 */


/****************
 * STEP : Creation du workspace
 * ************/
fileSep=File.separator

def dPathWorkspaceFile=new File(VAR_DSTAGE_Workspace)

logger("INFO","\n------------\nGestion initial du workspace:${VAR_DSTAGE_Workspace} \n------------\n","$VAR_DSTAGE_LogFileName")
if(dPathWorkspaceFile.isDirectory()){
	logger("INFO","Un workspace ($VAR_DSTAGE_Workspace) existe deja","$VAR_DSTAGE_LogFileName");
	if (new File("$VAR_DSTAGE_Workspace${fileSep}tag${fileSep}${VAR_DSTAGE_backupfull_file}").isFile()){
		logger("INFO"," Le full backfile (tag${fileSep}${VAR_DSTAGE_backupfull_file}) existe deja. Suppression partiel du workspace","$VAR_DSTAGE_LogFileName");
		def pathPkgDir="$VAR_DSTAGE_Workspace${fileSep}pkg"
		def dPathPkgDir=new File("$VAR_DSTAGE_Workspace${fileSep}pkg")
		
		logger("INFO","Suppression de $pathPkgDir si le repertoire existe","$VAR_DSTAGE_LogFileName");
		if(dPathPkgDir.isDirectory()){
			if(!dPathPkgDir.deleteDir()){
				logger("ERROR"," Impossible de supprimer le repertoire ${pathPkgDir}","$VAR_DSTAGE_LogFileName");
				System.exit(1)
			}
		}
	}
	else{
		logger("INFO","Le full backfile tag${fileSep}${VAR_DSTAGE_backupfull_file}) n'existe pas. Suppression complet du workspace","$VAR_DSTAGE_LogFileName");
		if(!dPathWorkspaceFile.deleteDir()){
			logger("ERROR","Impossible de supprimer le repertoire ${VAR_DSTAGE_Workspace}","$VAR_DSTAGE_LogFileName");
			System.exit(1)
		}
		
		
	}
}

logger("INFO","Creation des sous repertoires du workspace","$VAR_DSTAGE_LogFileName");
// loop sur chaque sous directory a creer
VAR_CST_DSTAGE_tmp_directories.split("\\|").each{ it->
	def String dirName=it.trim()
	if (dirName != "" ){
		File dir=new File("${VAR_DSTAGE_Workspace}${fileSep}${dirName}")
		if(!dir.isDirectory()){
			if(!dir.mkdirs()){
				logger("ERROR","Impossible de creer le sous repertoire :"+dir.getAbsolutePath(),"$VAR_DSTAGE_LogFileName");
				System.exit(1)
			}
			else{
				logger("INFO","Creation du sous repertoire :${dirName}","$VAR_DSTAGE_LogFileName");
			}
		}
		else{
			logger("INFO","Le sous repertoire :${dirName} existe deja","$VAR_DSTAGE_LogFileName");
		}
	}
}


/****************
 * STEP : download du package SCP
# -- get source package
source=/products/serena/sas/$baselineId/DATASTAGE/$Folder/$Package (10.66.11.14)
G:\Test\Serena\DIM\BL_PROJET_INITIAL_30.10.2013_15.21.51_9479\DATASTAGE\PROJET_INITIAL\Projet_Initial_WAP-6037_0.zip
destination=$VAR_DSTAGE_Workspace/pkg/$Package

if [ $? -ne 0 ] ; then echo "YYYYMMDD-HHMMSS: [ERROR] Impossible de recuperer le pacakge $Package" ; exit 1; else echo "YYYYMMDD-HHMMSS: [INFO] recuperation du pacakge $Package reussie" ; fi

 * ********/
def input = new File("${VAR_CST_DIM_SASDepot}\\${SBM_BaselineId}\\${SBM_Folder}\\${SBM_Projet}\\${SBM_Package}").newDataInputStream();  def output = new File("$VAR_DSTAGE_Workspace/pkg/${SBM_Package}").newDataOutputStream()
output << input
 input.close(); output.close()
/*****/
 
 /****************
  * STEP: decompression et Controle du package 
  ****************/

 
logger("INFO","\n------------\ndecompression du package\n------------\n","$VAR_DSTAGE_LogFileName")
 logger("INFO","Extraction du fichier zip \"${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${SBM_Package}\"","$VAR_DSTAGE_LogFileName")
 try{
	 def ant = new AntBuilder()// create an antbuilder
 
	 ant.unzip(  src:"${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${SBM_Package}",
			 dest:"${VAR_DSTAGE_Workspace}${fileSep}pkg",
			 overwrite:"false"
	 )
	 
	 
 }
 catch (Exception e) {
	 e.printStackTrace()
	 logger("ERROR","Erreur dans l'extraction du fichier zip \"${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${SBM_Package}\"","$VAR_DSTAGE_LogFileName")
	 logger("ERROR", e.getMessage(),"$VAR_DSTAGE_LogFileName")
	 System.exit(1)
 }
 
 // Recuperation du Parent-SubDirectory
 // => recherche du premier repertoire present dans ${VAR_DSTAGE_Workspace}${fileSep}pkg different de ^bck.*\$
  def String pkgParentSubDirectory=""
 try{
	 new File("${VAR_DSTAGE_Workspace}${fileSep}pkg").eachDir {
		 def File sdir=it
		 pkgParentSubDirectory=sdir.getName()
		 // !!!! Parent-SubDirectory trouve : on force l'arret de la boucle!!!!
		 throw new Exception("Parent-SubDirectory trouve")
	 }
	 
 }
 catch(Exception e){
	 // Parent-SubDirectory trouve
	 logger("DEBUG","pkgParentSubDirectory=$pkgParentSubDirectory","$VAR_DSTAGE_LogFileName")
 }
 if(pkgParentSubDirectory==""){
	 logger("ERROR","Impossible de trouver un sous repertoire dans${VAR_DSTAGE_Workspace}${fileSep}pkg","$VAR_DSTAGE_LogFileName")
	 System.exit(1)
 }
 
 
 

 /****************
  * STEP: Backup FULL
  ****************/

 
 
/**********
 * ????????????
 * Pour moi, ce test est unitil, il faut sauvegarder qquoiqu'il arrive
 *
 */
// recherche du fichier ISX a importer
logger("INFO","recherche du fichier ISX a importer","$VAR_DSTAGE_LogFileName")
//$Dstage_Job=$('find -name $VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory *.isx')
def pathIsxFile=""
try{
	new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}").eachFile  {
		def File f=it
		if(f.isFile() && f.getName() =~ "^.*\\.isx\$"){
			pathIsxFile=f.getName()
			//arret de la loop
			throw new Exception("ISX trouve")
		}
   }
}
catch(Exception e){
	// iSX trouve
}
if(pathIsxFile==""){
	logger("WARN","Aucun ISX n'a pas etre localise ds ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}","$VAR_DSTAGE_LogFileName")
	System.exit(1)
}
else{
	logger("INFO","L'ISX a impoter a ete identifie:  pkg${fileSep}${pkgParentSubDirectory}${fileSep}$pathIsxFile","$VAR_DSTAGE_LogFileName")
}

 
 logger("INFO","\n------------\nLancement du backup Full\n------------\n","$VAR_DSTAGE_LogFileName")
 VAR_DSTAGE_TargetServerList.split("\\|").each{
	 def String targetServerInst=it.trim()
	 logger("INFO","Lancement du backup Full pour l'instance ${targetServerInst}","$VAR_DSTAGE_LogFileName")
	 
	 
	 
	 
	 // copy et formatage du fichier model istool permettant l'export isx
	 //  cp $VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/istool_backup_full.txt $VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/istool_backup_full_$Current_Server.txt
	 logger("INFO","Formatage du template istool permettant l'export isx : pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full_${targetServerInst}.txt","$VAR_DSTAGE_LogFileName")
	 def Map transco=[:]
	 transco["[HOST]"]="${targetServerInst}"
	 transco["[FICARC]"]="${SBM_BckPackage}"
	 transco["[PROJET]"]="${SBM_Projet}"
	 transco["[PWD]"]="${SBM_Password}"
	 transco["[USER]"]="${SBM_Login}"
	 
	 
	 def File tplIstoolBackup=new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full.txt")
	 if(!tplIstoolBackup.isFile()){
		 logger("ERROR","Le template istools permettant l'export ISX n'ets pas accessible: ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full.txt","$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }
	 
	 def String srcTextData = tplIstoolBackup.text
	 transco.each {key, value ->
		 srcTextData=srcTextData.replace(key, value)
	 }
	 
	 //
	 def destDataInputStream = new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full_${targetServerInst}.txt").newDataOutputStream()
	 destDataInputStream << srcTextData
	 destDataInputStream.close()
	 logger("DEBUG","Transco Result :pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full_${targetServerInst}.txt:\n-----------------\n"+ new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full_${targetServerInst}.txt").text+"-----------------\n","$VAR_DSTAGE_LogFileName")
	 
	 
	 
	 // Creation du sous repertoire de backup ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}
	 if(!new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}").isDirectory()){
		 if(!new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}")){
			 logger("ERROR","Impossible de creer le sous repertoire ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 else{
			 logger("INFO","Creation du sous repertoire bck${fileSep}${targetServerInst}","$VAR_DSTAGE_LogFileName")
		 }
	 }
	 else{
		 
		 logger("WARN","Le sous repertoire ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst} existe deja.Suppression et re creation de ce dernier...","$VAR_DSTAGE_LogFileName")
		 if(!new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}").deleteDir()){
			 logger("ERROR","Impossible de supprimer le sous repertoire ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 if(!new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}").mkdirs()){
			 logger("ERROR","Impossible de creer le sous repertoire ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 
	 }
	 
	 // Reproduction de l'arborescense de sous repertoires contenu dans le package pour le sous repertoires de bck du target server $targetServerInst
	 logger("INFO","Reproduction de l'arborescense de sous repertoires contenu dans le package pour le sous repertoires de bck du target server $targetServerInst","$VAR_DSTAGE_LogFileName")
	 def baseAbsolutePath=new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}").getAbsolutePath()
	 
	 new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}").eachFileRecurse(groovy.io.FileType.DIRECTORIES) {
		 def String dirAbsolutePath=it.getAbsolutePath()
		 def String dirRelativePath=dirAbsolutePath.replace("${baseAbsolutePath}${fileSep}", "")

		 logger("TRACE","Creation de bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${dirRelativePath}","$VAR_DSTAGE_LogFileName")
		 if(!new File("${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${dirRelativePath}").mkdirs()){
			 logger("ERROR","Impossible de creer le sous repertoire ${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${dirRelativePath}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
	 
	 }
	 

	 // export ISX
	 try{
		 def command = """cmd /c ${VAR_CST_DSTAGE_IsTool_Path} -script ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_full_{targetServerInst}.txt"""// Create the String
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
			   logger("INF0","export ISX du $targetServerInst reussie","$VAR_DSTAGE_LogFileName")
		  }
		  else{
			  
			  logger("ERROR",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
			  logger("ERROR",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
			  logger("ERROR","echec de l'export ISX du $targetServerInst","$VAR_DSTAGE_LogFileName")
			  System.exit(1)
		  }
	 }
	 catch (Exception e){
		 logger("ERROR",e.getMessage(),"$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }
		  

	 
 }// end loop for each TargetServer
 
 /****
  * STEP
  * 
  ****/
 //  creation d'un fichier TAG marquant la presence d'un export FULL
 logger("INF0"," creation d'un fichier TAG marquant la presence d'un export FULL: tag${fileSep}${VAR_DSTAGE_backupfull_file}","$VAR_DSTAGE_LogFileName")
 
 def tagFile= new File("$VAR_DSTAGE_Workspace${fileSep}tag${fileSep}${VAR_DSTAGE_backupfull_file}")
 if(!tagFile.isFile()){
	 if(!tagFile.createNewFile()){
		 logger("ERROR","Impossible de creer le fichier TAG : $VAR_DSTAGE_Workspace${fileSep}tag${fileSep}${VAR_DSTAGE_backupfull_file} ","$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }else{
	 	logger("INFO","=> Creation du fichier TAG : tag${fileSep}${VAR_DSTAGE_backupfull_file} reussi ","$VAR_DSTAGE_LogFileName")
	 }
 }
 else{
 	logger("INFO","Le fichier TAG : $VAR_DSTAGE_Workspace${fileSep}tag${fileSep}${VAR_DSTAGE_backupfull_file} existe deja ","$VAR_DSTAGE_LogFileName")
 }
 
 // Fin du backup Full
 
 

// Generation d'un package de backup
logger("INFO","\n------------\nGeneration d'un package de backup tmp${fileSep}${SBM_BckPackage}.zip de bck${fileSep}\n------------\n","$VAR_DSTAGE_LogFileName")
def String zipName="${VAR_DSTAGE_Workspace}${fileSep}tmp${fileSep}${SBM_BckPackage}"
def String basedir="${VAR_DSTAGE_Workspace}"
try {
	ant = new AntBuilder()
	ant.zip(destfile: zipName, basedir:basedir,includes:"bck/**")
}
catch (Exception e) {
	logger("ERROR", "Error durant la creation du package de backup tmp${fileSep}${SBM_BckPackage}:\n ${e.message}","$VAR_DSTAGE_LogFileName")
	System.exit(1)
}



/***
 * STEP SCP vers DIM SRV $VAR_DSTAGE_Workspace/tmp/$BckPackage -> 10.66.11.14:/products/serena/depot/DATASTAGE/$SBM_Folder/$BckPackage
 * 
 */
println "SCP vers DIM SRV $VAR_DSTAGE_Workspace/tmp/${SBM_BckPackage} -> 10.66.11.14:/products/serena/depot/DATASTAGE/${SBM_Folder}/$SBM_BckPackage}"

/***
 * STEP SCP vers SBM SRV $VAR_DSTAGE_Workspace/tmp/$BckPackage -> 10.66.11.14:/products/serena/depot/DATASTAGE/$SBM_Folder/$BckPackage
 *
 */
println "SCP vers DIM SRV $VAR_DSTAGE_Workspace/tmp/${SBM_BckPackage} -> 10.66.39.70:/D:/products/LOG_NOLIO/$SBM_BckPackage}"

 
