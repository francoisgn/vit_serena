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

def dPathWorkspaceFile=new File("${VAR_DSTAGE_Workspace}")

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
				logger("TRACE","Creation du sous repertoire :${dirName}","$VAR_DSTAGE_LogFileName");
			}
		}
		else{
			logger("INFO","Le sous repertoire :${dirName} existe deja","$VAR_DSTAGE_LogFileName");
		}
	}
}

/****************
 * STEP : Creation du workspace
 * ************/








/****************
 * STEP : download du package SCP
# -- get source package
source=/products/serena/sas/$baselineId/DATASTAGE/$Folder/$Package (10.66.11.14)
G:\Test\Serena\DIM\BL_PROJET_INITIAL_30.10.2013_15.21.51_9479\DATASTAGE\PROJET_INITIAL\Projet_Initial_WAP-6037_0.zip
destination=$VAR_DSTAGE_Workspace/pkg/$Package

if [ $? -ne 0 ] ; then echo "YYYYMMDD-HHMMSS: [ERROR] Impossible de recuperer le pacakge $Package" ; exit 1; else echo "YYYYMMDD-HHMMSS: [INFO] recuperation du pacakge $Package reussie" ; fi

 * ********/
def input = new File("${CST_DIM_SASDepot}\\${SBM_BaselineId}\\${SBM_Folder}\\${SBM_Projet}\\${SBM_Package}").newDataInputStream();  def output = new File("$VAR_DSTAGE_Workspace/pkg/${SBM_Package}").newDataOutputStream()
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
 /************************************************
  *  Rappel Structure d'un package
		Projet_Initial_WAP-6037_0/
		Projet_Initial_WAP-6037_0/00-Dossier_Exploitation
		Projet_Initial_WAP-6037_0/00-Dossier_Exploitation/dossier dexploitation Master_Gcons_PDO.doc
		Projet_Initial_WAP-6037_0/00-Dossier_Exploitation/dossier dexploitation Master_Gcons_TEDI.doc
		Projet_Initial_WAP-6037_0/00-Dossier_Exploitation/Echainement Traitements referentiel SPI.xls
		Projet_Initial_WAP-6037_0/00-Dossier_Exploitation/Le referentiel SPI.doc
		Projet_Initial_WAP-6037_0/01-Marche_a_suivre
		Projet_Initial_WAP-6037_0/01-Marche_a_suivre/Marche a suivre SERENA.docx
		
		// a copier sur le NAS dans l'arbo projet
		Projet_Initial_WAP-6037_0/02-NAS
		Projet_Initial_WAP-6037_0/02-NAS/Documentation
		Projet_Initial_WAP-6037_0/02-NAS/Documentation/dossier d'exploitation Master_Gprox_TIGRE.doc
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_manuels
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_manuels/Serena
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_manuels/Serena/Fichier_manuel.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_manuels/Serena/toto.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie/Serena
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie/Serena/doc1.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie/Serena/doc2.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie/Serena/doc3.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_sortie/Serena/doc4.txt
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_temporaires
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_temporaires/Serena
		Projet_Initial_WAP-6037_0/02-NAS/Fichiers_temporaires/Serena/Le super fichier de la mort qui tue.txt
		Projet_Initial_WAP-6037_0/02-NAS/Init
		Projet_Initial_WAP-6037_0/02-NAS/Init/ListeDesjobs_Serena_Mensuel.txt
		Projet_Initial_WAP-6037_0/02-NAS/Init/Serena.ini
		Projet_Initial_WAP-6037_0/02-NAS/Sql
		Projet_Initial_WAP-6037_0/02-NAS/Sql/Serena
		Projet_Initial_WAP-6037_0/02-NAS/Sql/Serena/Script qui dechire sa race.sql
		Projet_Initial_WAP-6037_0/02-NAS/Stockage
		Projet_Initial_WAP-6037_0/02-NAS/Stockage/Serena
		
		// a copier sur les instances (3 servers) dans l'arborescence projet) s: est un disque local
		Projet_Initial_WAP-6037_0/03-ArboDisques-S
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_hashcodes
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_hashcodes/Serena
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_hashcodes/Serena/toto
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_temporaires
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_temporaires/Serena
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_temporaires/Serena/Fichier_ephemere.txt
		Projet_Initial_WAP-6037_0/03-ArboDisques-S/fichiers_temporaires/Serena/titi.txt
		
		// a copier sur les instances (3 servers) dans l'arborescence projet) M: est un disque local
		// Yop: il faut reproduire l'arborescence
		// pas besoin de sauvegarde des fichiers mais uniquement de l'arborescence
		Projet_Initial_WAP-6037_0/05-ArboDisques-M
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Compte Rendu
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Compte Rendu/Serena
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Suivi
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Suivi/Master
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Suivi/TR
		Projet_Initial_WAP-6037_0/05-ArboDisques-M/Suivi/Validation
		
		// pour le backup et l'import datastage
		Projet_Initial_WAP-6037_0/istool.log
		Projet_Initial_WAP-6037_0/istool_backup.txt
		Projet_Initial_WAP-6037_0/istool_backup_full.txt
		Projet_Initial_WAP-6037_0/Projet_Initial_REF_INI.xml
		Projet_Initial_WAP-6037_0/Projet_Initial_WAP-6037_0.isx
*************************************************************/
  // Apres decompression on a 
  // d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\pkg\Projet_Initial_WAP-6037_0
 //  d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\pkg\Projet_Initial_WAP-6037_0\00-Dossier_Exploitation
 //  d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\pkg\Projet_Initial_WAP-6037_0\contenu du package
 //  
 
//  ...
 // Recuperation du Parent-SubDirectory
 // dans l'exemple on cherche d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\pkg\ le repertoire pkgParentSubDirectory=Projet_Initial_WAP-6037_0
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
 
 // Force la creation des sous repertoires obligatoires
 logger("INFO","Force la creation des sous repertoires obligatoires sous pkg${fileSep}${pkgParentSubDirectory}","$VAR_DSTAGE_LogFileName")
 def Boolean pkgValidity=true
 CST_DSTAGE_ArboDisque_Files.split("\\|").each{
	 def String sdir=it.trim()
	 def File mandatorySubDir=new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}${sdir}")
	 if(!mandatorySubDir.isDirectory()){
		 if(! mandatorySubDir.mkdirs()){
			 logger("ERROR","Impossible de creer le sous repertoire "+mandatorySubDir.getAbsolutePath() +"","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 } 
		 logger("TRACE","Creation de pkg${fileSep}${pkgParentSubDirectory}${fileSep}${sdir} ","$VAR_DSTAGE_LogFileName")
	 }
	 else{
		 logger("TRACE","Le sous repertoire pkg${fileSep}${pkgParentSubDirectory}${fileSep}${sdir}  existe deja. Rien a faire...","$VAR_DSTAGE_LogFileName")
	 }
 }
 /****************
  * STEP: decompression et Controle du package
  ****************/
 
 
 
 
 
 
 
 
 /****************
  * STEP: Backup Partiel
  ****************/
 //VARIABLES
 fileSep=File.separator
 
 
 
 
 logger("INFO","\n------------\nLancement du backup Partiel\n------------\n","$VAR_DSTAGE_LogFileName")
 VAR_DSTAGE_TargetServerList.split("\\|").each{
	 def String targetServerInst=it.trim()
	 
	 // Debut Step Creation rep de backup
	 // Creation du sous repertoire de backup ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${targetServerInst}
	 // ici dans l'exemple for each Server_target {mkdir -p  d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\bck\${targetServerInst} }
	 
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
	 // ex: reproduction des sous rep de d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\pkg\Projet_Initial_WAP-6037_0\ d:\Deploy\Serena\BL_PROJET_INITIAL_30.10.2013_15.21.51_9470\bck\${targetServerInst}
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
	 // Fin Step Creation rep de backup
	 
	 // Debut Step Backup des fichiers sur M
	 // def CST_DSTAGE_M_Folders="05-ArboDisques-M\\Suivi\\Master|05-ArboDisques-M\\Suivi\\TR|05-ArboDisques-M\\Suivi\\Validation"
     // VAR_DSTAGE_M_Target_Folder="${VAR_DSTAGE_Fichiers_Master}|${VAR_DSTAGE_Fichiers_TR}|${VAR_DSTAGE_Fichiers_Validation}"
	 logger("INFO","Backup des fichiers sur M","$VAR_DSTAGE_LogFileName")
	 def DSTAGE_M_FoldersTab=CST_DSTAGE_M_Folders.split("\\|")
	 def DSTAGE_M_Target_FolderTab=VAR_DSTAGE_M_Target_Folder.split("\\|")
	 def int counter
	 for (counter=0;counter<DSTAGE_M_FoldersTab.size();counter++){
		 def String Dstage_Folder=DSTAGE_M_FoldersTab[counter]
		 def String Dstage_M_Folder=DSTAGE_M_Target_FolderTab[counter]
		 
		 logger("INFO","Recherche de la liste des fichiers (path relatif) a sauvegarder depuis pkg${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}","$VAR_DSTAGE_LogFileName")
		 def dstage_backup_files=[]
		 new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}").eachFile() {
			 dstage_backup_files.add(it.getName())
			 //println "dirRelativePath="+it.getName()
		 }
		 logger("TRACE","Liste dstage_backup_files "+dstage_backup_files,"$VAR_DSTAGE_LogFileName")
		 
		 // Backup des fichiers de l'instance ${targetServerInst}
		 //logger("INFO","Backup des fichiers de l'instance ${targetServerInst} ","$VAR_DSTAGE_LogFileName")
		 dstage_backup_files.each{ backupFile->
			 def sourceTarget="${Dstage_M_Folder}${fileSep}${backupFile}"
			 def String destTarget="${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}"
			 logger("INFO","Backup de ${Dstage_M_Folder}${fileSep}${backupFile} dans bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}","$VAR_DSTAGE_LogFileName")
			 // cas ou ${sourceTarget} existe et est un repertoire
			 if(new File("${sourceTarget}").isDirectory()){
				 logger("TRACE","cp -r ${sourceTarget} ${destTarget}","$VAR_DSTAGE_LogFileName")
				 def ant = new AntBuilder()
				 try {
						  ant.copy(verbose: "true", todir: "${destTarget}", overwrite:'true',includeEmptyDirs:'true') {
							 fileset(dir: "${sourceTarget}"){ include(name:"**/*") }
						 }
				 }
				 catch (Exception e) {
					 logger("ERROR","cp -rf ${sourceTarget} ${destTarget} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
					 System.exit(1)
				 }
			 }
			 // cas ou ${sourceTarget} existe et est un fichier
			 else if(new File("${sourceTarget}").isFile()){
				 def sourceTargetBasename= new File ("sourceTarget").getName()
				 logger("TRACE","cp -f ${sourceTarget} ${destTarget}${fileSep}${sourceTargetBasename}","$VAR_DSTAGE_LogFileName")
				 def ant = new AntBuilder()
				 try {
						  ant.copy(verbose: "true",file:"${sourceTarget}", tofile: "${destTarget}${fileSep}${sourceTargetBasename}", overwrite:'true')
					 
				 }
				 catch (Exception e) {
					 logger("ERROR","cp -f ${sourceTarget} ${destTarget}${fileSep}${sourceTargetBasename} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
					 System.exit(1)
				 }
			 }
			 else{
				 logger("TRACE","le fichier ou repertoire ${sourceTarget} n'existe pas. Pas de backup necessaire","$VAR_DSTAGE_LogFileName")
			 }
		 }
		 
	 }
	 
	 // Fin Step Backup des fichiers sur M
	 
	 // Debut Step Backup des fichiers sur NAS
	 // Rappel:
	 // CST_DSTAGE_NAS_Folders="02-NAS/Fichiers_manuels|02-NAS/Fichiers_sortie|02-NAS/Fichiers_temporaires|02-NAS/Init|02-NAS/Sql|02-NAS/Stockage"
	 // VAR_DSTAGE_Nas_Target_Folder="${VAR_DSTAGE_Fichiers_Manuels_Directory}|${VAR_DSTAGE_Fichiers_Sortie}|${VAR_DSTAGE_Fichiers_Temporaires}|${VAR_DSTAGE_Fichiers_Init}|${VAR_DSTAGE_Fichiers_Sql}|${VAR_DSTAGE_Fichiers_Stockage}"
	 // VAR_DSTAGE_Fichiers_Manuels_Directory="${Cst_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_manuels"
	 // VAR_DSTAGE_Fichiers_Sortie="${Cst_Nas_Projet_Path}${SBM_Projet}${fileSep}Fichiers_sortie"
	 // VAR_DSTAGE_Fichiers_Temporaires="${Cst_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Fichiers_temporaires"
	 // VAR_DSTAGE_Fichiers_Stockage="${Cst_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Stockage"
	 // VAR_DSTAGE_Fichiers_Sql="${Cst_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}SQL"
	 // VAR_DSTAGE_Fichiers_Init="${Cst_Nas_Projet_Path}${fileSep}${SBM_Projet}${fileSep}Init"
	 
	 logger("INFO","Backup des fichiers sur NAS","$VAR_DSTAGE_LogFileName")
	 def String[] DSTAGE_NAS_FoldersTab
	 DSTAGE_NAS_FoldersTab=CST_DSTAGE_NAS_Folders.split("\\|")
	 def String [] VAR_DSTAGE_Nas_Target_FolderTab
	 VAR_DSTAGE_Nas_Target_FolderTab=VAR_DSTAGE_Nas_Target_Folder.split("\\|")
	 
	 
	 for (counter=0;counter<DSTAGE_NAS_FoldersTab.size();counter++){
		 def String Dstage_Folder=DSTAGE_NAS_FoldersTab[counter]
		 def String Dstage_NAS_Folder=VAR_DSTAGE_Nas_Target_FolderTab[counter]
		 //println "[D] Dstage_Folder=$Dstage_Folder"
		 //println "[D] Dstage_NAS_Folder=$Dstage_NAS_Folder"
		 
		 // Recherche des fichiers a Backuper
		 logger("INFO","Recherche de la liste des fichiers (path relatif) a sauvegarder depuis pkg${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}","$VAR_DSTAGE_LogFileName")
		 def dstage_backup_files=[]
		 new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}").eachFile() {
			 dstage_backup_files.add(it.getName())
			 //println "dirRelativePath="+it.getName()
		 }
		 logger("TRACE","Liste dstage_backup_files "+dstage_backup_files,"$VAR_DSTAGE_LogFileName")
		 
		 // Backup des fichiers de l'instance ${targetServerInst}
		 //logger("INFO","Backup des fichiers de l'instance ${targetServerInst} ","$VAR_DSTAGE_LogFileName")
		 dstage_backup_files.each{ backupFile->
			 def sourceTarget="${Dstage_NAS_Folder}${fileSep}${backupFile}"
			 def String destTarget="${VAR_DSTAGE_Workspace}${fileSep}bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}"
			 logger("INFO","Backup de ${Dstage_NAS_Folder}${fileSep}${backupFile} dans bck${fileSep}${targetServerInst}${fileSep}${pkgParentSubDirectory}${fileSep}${Dstage_Folder}","$VAR_DSTAGE_LogFileName")
			 // cas ou ${sourceTarget} existe et est un repertoire
			 if(new File("${sourceTarget}").isDirectory()){
				 logger("TRACE","cp -r ${sourceTarget} ${destTarget}","$VAR_DSTAGE_LogFileName")
				 def ant = new AntBuilder()
				 try {
						  ant.copy(verbose: "true", todir: "${destTarget}", overwrite:'true',includeEmptyDirs:'true') {
							 fileset(dir: "${sourceTarget}"){ include(name:"**/*") }
						 }
				 }
				 catch (Exception e) {
					 logger("ERROR","cp -rf ${sourceTarget} ${destTarget} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
					 System.exit(1)
				 }
			 }
			 // cas ou ${sourceTarget} existe et est un fichier
			 else if(new File("${sourceTarget}").isFile()){
				 def sourceTargetBasename= new File ("sourceTarget").getName()
				 logger("TRACE","cp -f ${sourceTarget} ${destTarget}${fileSep}${sourceTargetBasename}","$VAR_DSTAGE_LogFileName")
				 def ant = new AntBuilder()
				 try {
						  ant.copy(verbose: "true",file:"${sourceTarget}", tofile: "${destTarget}${fileSep}${sourceTargetBasename}", overwrite:'true')
					 
				 }
				 catch (Exception e) {
					 logger("ERROR","cp -f ${sourceTarget} ${destTarget}${fileSep}${sourceTargetBasename} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
					 System.exit(1)
				 }
			 }
			 else{
				 logger("TRACE","le fichier ou repertoire ${sourceTarget} n'existe pas. Pas de backup necessaire","$VAR_DSTAGE_LogFileName")
			 } 
		 }
		 
	 }
	 
	 
	 
	 // Test Presence dans le package de .../pkg/$pkgParentSubDirectory/*.isx
	 def pathIsxFile=""
	 try{
		 new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}").eachFile  {
			 def File f=it.trim()
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
		 // en cas de non presence d'un ISX, il est inutil de backup le projet via export ISX, car ce dernier ne sera donc pas relivre
		 logger("WARN","Aucun ISX n'a pas etre localise ds ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}\n=> Pas de lancement de backup du projet via export ISX necessaire","$VAR_DSTAGE_LogFileName")
		 
	 }
	 else{
		 // en cas de presence d'un ISX, on lance la procedure de backup ISX dans le cas ou le projet aurait deja ete deploye prealablement
		 logger("INFO","L'ISX a impoter a ete identifie:  pkg${fileSep}${pkgParentSubDirectory}${fileSep}$pathIsxFile\n=> Lancement du Backup ISX du projet via un export ISX","$VAR_DSTAGE_LogFileName")
		 
	 
	 
		 // copy et formatage du fichier model istool permettant l'export isx
		 //  cp $VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/istool_backup.txt $VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/istool_backup_$Current_Server.txt
		 logger("INFO","Formatage du template istool permettant l'export isx : pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_${targetServerInst}.txt","$VAR_DSTAGE_LogFileName")
		 def Map transco=[:]
		 transco["[HOST]"]="${targetServerInst}"
		 transco["[FICARC]"]="${SBM_BckPackage}"  // precise le nom de l'export ISX dans le cadre du backup d'un projet de meme nom deja existant
		 transco["[PROJET]"]="${SBM_Projet}"
		 transco["[PWD]"]="${SBM_Password}"
		 transco["[USER]"]="${SBM_Login}"
		 
		 
		 def File tplIstoolBackup=new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup.txt")
		 // test l'existance du template .../pkg/$pkgParentSubDirectory/istool_backup.txt
		 if(!tplIstoolBackup.isFile()){
			 logger("ERROR","Le template istools permettant l'export ISX n'ets pas accessible: ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup.txt","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 // chargement dans la  variable srcTextData du contenu du fichier template .../pkg/$pkgParentSubDirectory/istool_backup.txt
		 def String srcTextData = tplIstoolBackup.text
		 
		 // pour chaque couple token,value de la Map, on applique un replace dans la variable srcTextData sur elle meme
		 transco.each {key, value -> 
			 srcTextData=srcTextData.replace(key, value)
		 }
		 
		 // Generation du fichier istool_backup_${targetServerInst}.txt specifique a cette targetServerInst
	     def destDataInputStream = new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_${targetServerInst}.txt").newDataOutputStream()
		 destDataInputStream << srcTextData
		 destDataInputStream.close()
		 
		 logger("DEBUG","Transco Result :pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_${targetServerInst}.txt:\n-----------------\n"+ new File("${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}istool_backup_${targetServerInst}.txt").text+"-----------------\n","$VAR_DSTAGE_LogFileName")
		 
		 // Test si un backup Full n'a pas deja ete genere
		 if (!new File("$VAR_DSTAGE_Workspace${fileSep}tag${fileSep}${VAR_DSTAGE_backupfull_file}").isFile()){
			 // cas ou il n' y a pas de backup Full
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
			  
			
	
		 }
		 else{
			 logger("INFO","Un Backup Full tag${fileSep}${VAR_DSTAGE_backupfull_file}) existe, un export des ISX est inutile, car deja fait...","$VAR_DSTAGE_LogFileName")
		 }
	 }
	 
	 //
	 
 }// end loop for each TargetServer
 logger("INFO","-----Fin du backup Partiel----------\n\n","$VAR_DSTAGE_LogFileName")
 // Fin du backup Partiel
 
 
 /****************
  * STEP: Backup Partiel
  ****************/
 
 
 
 
 
 /***************************
  * STEP
  ***************************/
 
 logger("INFO","\n------------\nDeploiement de l'ISX \n------------\n","$VAR_DSTAGE_LogFileName")
 
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
	 logger("ERROR","Aucun ISX n'a pas etre localise ds ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}","$VAR_DSTAGE_LogFileName")
	 System.exit(1)
 }
 else{
	 logger("INFO","L'ISX a impoter a ete identifie:  pkg${fileSep}${pkgParentSubDirectory}${fileSep}$pathIsxFile","$VAR_DSTAGE_LogFileName")

	// Deploiement
	logger("INFO","Lancement du Deploiement","$VAR_DSTAGE_LogFileName")
	 
	logger("INFO","Deploiement de ISX sur chacune des instances","$VAR_DSTAGE_LogFileName")
	VAR_DSTAGE_TargetServerList.split("\\|").each{
		 def String targetServerInst=it.trim()
		 logger("INFO","Import ISX et complilation des JOBS sur instance $targetServerInst","$VAR_DSTAGE_LogFileName")
		 try{
			 logger("INFO","Import ISX sur ${targetServerInst}","$VAR_DSTAGE_LogFileName")
			 command="""cmd /c ${CST_DSTAGE_IsTool_Path} import -domain ${targetServerInst}:${CST_DSTAGE_Port_Domaine} -username ${SBM_Login} -password ${SBM_Password} -verbose -archive "${VAR_DSTAGE_Workspace}/pkg/${pkgParentSubDirectory}/${pathIsxFile}" -replace -ds '"${targetServerInst}:31538/${SBM_Projet}"'"""	 
			 command="""cmd /c echo $command"""
			 logger("INFO","cmd:${command}","$VAR_DSTAGE_LogFileName")
			 StringBuffer sbout = new StringBuffer()
			 StringBuffer sberr = new StringBuffer()
			 def process = command.execute(null, new File("${VAR_DSTAGE_Workspace}/pkg/${pkgParentSubDirectory}")) // Call *execute* on the string
			 def tout = process.consumeProcessOutputStream(sbout)
			 def terr = process.consumeProcessErrorStream(sberr)
			 
			  process.waitFor()                               // Wait for the command to finish
			// Obtain status and output
			  def value = process.exitValue()
	
			  logger("TRACE","return code: ${value}","$VAR_DSTAGE_LogFileName")
			  if(value==0){
				  logger("TRACE",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("TRACE",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("INF0","import ISX ${pathIsxFile} sur $targetServerInst reussie","$VAR_DSTAGE_LogFileName")
			  }
			  else{
				  
				  logger("ERROR",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("ERROR",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("ERROR","echec l'import ISX ${pathIsxFile} sur $targetServerInst","$VAR_DSTAGE_LogFileName")
				  System.exit(1)
			  }
		 }
		 catch (Exception e){
			 logger("ERROR",e.getMessage(),"$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 
		 // compilation job non compiles
		 try{
			 logger("INFO","compilation des jobs non compiles sur ${targetServerInst}","$VAR_DSTAGE_LogFileName")
			 command="""dscc /d ${targetServerInst}:${CST_DSTAGE_Port_Domaine} /H ${targetServerInst} /U ${SBM_Login} /P ${SBM_Password} ${SBM_Projet} /J* /R* /OUC """
			 command="""cmd /c echo $command"""
			 logger("INFO","cmd:${command}","$VAR_DSTAGE_LogFileName")
			 StringBuffer sbout = new StringBuffer()
			 StringBuffer sberr = new StringBuffer()
			 def process = command.execute(null, new File("${CST_DSTAGE_Root_Directory}")) // Call *execute* on the string
			 def tout = process.consumeProcessOutputStream(sbout)
			 def terr = process.consumeProcessErrorStream(sberr)
			 
			  process.waitFor()                               // Wait for the command to finish
			// Obtain status and output
			  def value = process.exitValue()
	
			  logger("TRACE","return code: ${value}","$VAR_DSTAGE_LogFileName")
			  if(value==0){
				  
				   logger("TRACE",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("TRACE",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("TRACE","compilation de tous les jobs non compile reussie sur $targetServerInst ","$VAR_DSTAGE_LogFileName")
			  }
			  else{
				  
				  logger("ERROR",'std err: ' +sberr.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("ERROR",'std out: ' +sbout.toString(),"$VAR_DSTAGE_LogFileName")
				  logger("ERROR","echec de la compilation de tous les jobs non compile sur $targetServerInst","$VAR_DSTAGE_LogFileName")
				  System.exit(1)
			  }
		 }
		 catch (Exception e){
			 logger("ERROR",e.getMessage(),"$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
	 }
}


 /***************************
  * STEP
  ***************************/

 
 def int transfertToNas(def String sourceTarget,def String destTarget,def String bckTarget,def String VAR_DSTAGE_LogFileName ){
	 def String fileSep=File.separator
	 println "DEBUG:sourceTarget=$sourceTarget"
	 println "DEBUG:destTarget=$destTarget"
	 println "DEBUG:bckTarget=$bckTarget"
	 if(new File(sourceTarget).isDirectory()){
		 if(!new File("${destTarget}").isDirectory()){
			 logger("ERROR","Le repertoire destination ${destTarget} n'existe pas: ","$VAR_DSTAGE_LogFileName")
			 return 1
		 }
		 logger("INFO","Deploiement des donnees de ${destTarget}: ","$VAR_DSTAGE_LogFileName")
		 logger("TRACE","cp -r ${sourceTarget} ${destTarget}","$VAR_DSTAGE_LogFileName")
		 def ant = new AntBuilder()
		 try {
				 ant.copy(verbose: "true", todir: "${destTarget}", overwrite:'true',includeEmptyDirs:'true') {
					 fileset(dir: "${sourceTarget}"){ include(name:"**/*") }
				 }
		 }
		 catch (Exception e) {
			 logger("ERROR","cp -rf ${sourceTarget} ${destTarget} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
			 return 1
		 }
		 
		 /********************
		  *  A revoir
		  * ?????
		  * *****************/
		 if(bckTarget != ""){
			 if(!new File("${bckTarget}").isDirectory()){
				 logger("ERROR","Le repertoire de backup ${bckTarget} n'existe pas: ","$VAR_DSTAGE_LogFileName")
				 return 1
			 }
			 logger("INFO","Backup des donnees dans ${bckTarget}","$VAR_DSTAGE_LogFileName")
			 logger("TRACE","cp -r ${sourceTarget} ${bckTarget}","$VAR_DSTAGE_LogFileName")
			 try {
				  ant.copy(verbose: "true", todir: "${bckTarget}", overwrite:'true',includeEmptyDirs:'true') {
					 fileset(dir: "${sourceTarget}"){ include(name:"**/*") }
				 }
			 }
			 catch (Exception e) {
				 logger("ERROR","cp -rf ${sourceTarget} ${bckTarget}  failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
				 return 1
			 }
		 }
	 }
	 else if(new File(sourceTarget).isFile()){
		 def sourceTargetBasename= new File ("${sourceTarget}").getName()
		 if(new File("${destTarget}").isDirectory()){
			 // cas ou destTarget represente le full path du repertoire distant ou doit etre copie le fichier source => ajout du basename du fichier source au destTarget
			 destTarget="${destTarget}${fileSep}${sourceTargetBasename}"
		 }
		 else{
			 // cas ou destTarget represente le full path du fichier distant
			 // => Controle que le repertoire parent du fichier distant existe
			 def File destParentDirectory=new File("${destTarget}").getParentFile()
			 if(!destParentDirectory.isDirectory()){
				 logger("ERROR","Le repertoire de  ${destParentDirectory.absolutePath} n'existe pas: ","$VAR_DSTAGE_LogFileName")
				 return 1
			 }
		 }
		 
		 logger("INFO","Transfert de fichier vers le NAS de ${sourceTarget}","$VAR_DSTAGE_LogFileName")
		 logger("TRACE","cp -f ${sourceTarget} ${destTarget}","$VAR_DSTAGE_LogFileName")
		 
		 def ant = new AntBuilder()
		 
		 try {
				 ant.copy(verbose: "true",file:"${sourceTarget}", tofile: "${destTarget}", overwrite:'true',includeEmptyDirs:'true') 
		 }
		 catch (Exception e) {
			 logger("ERROR","cp -f ${sourceTarget} ${destTarget} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
			 return 1
		 }
		 
		 /********************
		  *  A revoir
		  * ?????
		  * *****************/
		 if(bckTarget != ""){
			 if(!new File("${bckTarget}").isDirectory()){
				 logger("ERROR","Le repertoire de backup ${bckTarget} n'existe pas: ","$VAR_DSTAGE_LogFileName")
				 return 1
			 }
			 // cas ou destTarget represente le full path du repertoire distant ou doit etre copie le fichier source => ajout du basename du fichier source au destTarget
			 bckTarget="${bckTarget}${fileSep}${sourceTargetBasename}"
			 
			 logger("INFO","Backup des donnees dans ${bckTarget}","$VAR_DSTAGE_LogFileName")
			 logger("TRACE","cp -f ${sourceTarget} ${bckTarget}","$VAR_DSTAGE_LogFileName")
			 try {
				  ant.copy(verbose: "true", file:"${sourceTarget}", tofile: "${bckTarget}", overwrite:'true',includeEmptyDirs:'true') 
			 }
			 catch (Exception e) {
				 logger("ERROR","cp -f ${sourceTarget} ${bckTarget}  failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
				 return 1
			 }
		 }
	 }
	 else{
		 logger("INFO","Le repertoire ${sourceTarget} n'existe pas. => Pas de Transfert vers le NAS...","$VAR_DSTAGE_LogFileName")
	 }
	 return 0
 }
 
 // Livraison des données sur le NAS
 logger("INFO","\n------------\nDeploiement des données sur le NAS \n------------\n","$VAR_DSTAGE_LogFileName")
 
 /**********************
  * ???
  * A CONFIRMER: en cas d'erreur est ce que l'on arret le process de deploy
  * A CONFIRMER: en cas de non presence d'un sous sourceTarget, est ce que l'on arret ou est ce que l'on continue 
  * ???
  *********************/
 def String sourceTarget
 def String destTarget
 def String bckTarget
 def int cr
 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Fichiers_manuels"
 destTarget="${VAR_DSTAGE_Fichiers_Manuels_Directory}"
 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
 if(cr!= 0) { System.exit(1)}
 
 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Fichiers_sortie"
 destTarget="${VAR_DSTAGE_Fichiers_Sortie}"
 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
 if(cr!= 0) { System.exit(1)}
 
 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Fichiers_temporaires"
 destTarget="${VAR_DSTAGE_Fichiers_Temporaires}"
 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
 if(cr!= 0) { System.exit(1)}
 
 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Sql"
 destTarget="${VAR_DSTAGE_Fichiers_Sql}"
 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
 if(cr!= 0) { System.exit(1)}
 
 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Stockage"
 destTarget="${VAR_DSTAGE_Fichiers_Stockage}"
 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
 if(cr!= 0) { System.exit(1)}
 
 
 
 //_REF_INI.xml
 
 /***************************
  * STEP
  ***************************/
 import javax.xml.namespace.QName;
 import javax.xml.xpath.*;
 import org.w3c.dom.NodeList;
 import org.xml.sax.InputSource;
 def Map xpath_execute(def String xPathQuery,def String xmlFilePath){
	 Map map=[:]
	 map.put("count", 0)
	 map.put("firstElement","")
	 map.put("firstElement","")
	 map.put("allElements", null)
	 map.put("errorMessage",null)
	 
	 def NodeList result;
	 try{
		 // create xml source
		 InputSource source = new InputSource(new FileInputStream(xmlFilePath));
		
		 // create xpath engine
		 XPathFactory fabrique = XPathFactory.newInstance();
		 XPath xpath = fabrique.newXPath();
		 
		 // xpath query eval
		 XPathExpression exp = xpath.compile(xPathQuery);
		 result = (NodeList)exp.evaluate(source, XPathConstants.NODESET);
		 
		 if (result.getLength() >= 1) {
			 map.put("count", result.getLength())
			 map.put("firstElement", result.item(0).getTextContent())
			 map.put("lastElement", result.item(result.getLength()-1).getTextContent())
			 def tabElement=[]
			 for (int i = 0; i < result.getLength(); i++) {
				 tabElement[i]=result.item(i).getTextContent();
			 }
			 map.put("allElements", tabElement)
			 map.put("errorMessage","")
			 
		 }
		 else {
			 map.put("count", 0)
			 map.put("firstElement", result.item(0).getTextContent())
			 map.put("lastElement",result.item(0).getTextContent())
			 map.put("allElements", null)
			 map.put("errorMessage","")
		 }
		 
		 map.put("length", null)
	 }
	 catch (FileNotFoundException fe){
		 map.put("errorMessage", "[E] FileNotFoundException: "+fe.getMessage())
	 }
	 catch (XPathExpressionException xe){
		 map.put("errorMessage", "[E] XPathExpressionException: "+xe.getMessage())
	 }
	 
	 catch(Exception e){
		 map.put("errorMessage", "[E] Exception: "+e.getMessage())
	 }
	 return map
 }
 
 /*
  * 
  * STEP
  */
 
import org.ini4j.Wini
 
 

 
logger("INFO","\n------------\nGestion du parametrage INI et TXT \n------------\n","$VAR_DSTAGE_LogFileName")
def String projectRefIni="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}${SBM_Projet}_REF_INI.xml"
if (! new File(projectRefIni).isFile()){
	 logger("ERROR","Le fichier ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}${SBM_Projet}_REF_INI.xml n'est pas present...","$VAR_DSTAGE_LogFileName")
	 System.exit(1)
	 /**********************
	  * ???
	  * A CONFIRMER: 
	  * A CONFIRMER: en cas de non presence du fichier ${SBM_Projet}_REF_INI.xml, est ce que l'on arret ou est ce que l'on continue
	  * ???
	  *********************/
}
else{
	 logger("INFO","Le fichier ${SBM_Projet}_REF_INI.xml est present...","$VAR_DSTAGE_LogFileName")
	 
	 //  Recuperation de la liste de fichier INI definis  dans le fichier ${SBM_Projet}_REF_INI.xml  au format "init\file.ini"
	 logger("INFO","Recuperation de la liste de fichier INI definis  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
	 def Map mapR=xpath_execute("/IniFiles/file/@name",projectRefIni)
	 if (mapR["errorMessage"] != ""){
		 logger("ERROR", "Impossible de recuperer les noms de INI  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
		 logger("ERROR",mapR["errorMessage"],"$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }
	 def dstage_ini_files=mapR["allElements"]
	 logger("TRACE","dstage_ini_files="+dstage_ini_files,"$VAR_DSTAGE_LogFileName")
	 
	 //  Recuperation de la liste de fichier TXT presents dans ${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Init
	 def pathIniFile="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Init"
	 def  Dstage_Txt_Files=[]
	 logger("INFO","Recuperation de la liste de fichier TXT presents dans le repertoire pathIniFile","$VAR_DSTAGE_LogFileName")
	 new File(pathIniFile).eachFile{
		 def File f=it
		 if(f.isFile() && f.getName() =~ "^.*\\.[tT][xX][tT]\$"){
			 Dstage_Txt_Files.add(f.getName())
		 }
	 }
	 logger("TRACE","Dstage_Txt_Files="+Dstage_Txt_Files,"$VAR_DSTAGE_LogFileName")
	 
	 // Recuperation du nom de l'environnement au bon format
	 logger("INFO","Recuperation du nom de l'environnement au bon format","$VAR_DSTAGE_LogFileName")
	 def String VAR_Environnement=""
	 switch(SBM_Environnement){
		 case "Production" :VAR_Environnement="PRODUCTION";break;
		 case "Pre-Production" :VAR_Environnement="PREPRODUCTION";break;
		 case "Recette" :VAR_Environnement="RECETTE";break;
		 default: break;
	
	 }
	 logger("TRACE","Environnement:${SBM_Environnement}->${VAR_Environnement} ","$VAR_DSTAGE_LogFileName")
	 if(VAR_Environnement == ""){
		 logger("ERROR", "La valeur de l'environnement $SBM_Environnement n'est pas prise en charge. Seule les valeurs Production|Pre-Production|Recette son prise en compte","$VAR_DSTAGE_LogFileName")
		 System.exit(1)
	 }
	 
	 // ReCopie des fichiers TXT
	 logger("INFO","Traitement des fichiers .txt du répertoire Init ","$VAR_DSTAGE_LogFileName")
	 destTarget="${VAR_DSTAGE_Fichiers_Init}"
	 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	 Dstage_Txt_Files.each {  it->
		 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Init${fileSep}"+it.trim()
		 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
		 if(cr!= 0) { System.exit(1)}
	 }
	
	 // Valorisation des fichiers INI
	 logger("INFO","Valorisation des fichiers INI presents sous pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}Init${fileSep}","$VAR_DSTAGE_LogFileName")
	 dstage_ini_files.each{it->
		 def String iniFile=it.trim()
		 sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}02-NAS${fileSep}${iniFile}"
		 destTarget="${VAR_DSTAGE_Workspace}${fileSep}tmp${fileSep}${iniFile}"
		 /* remarque 1: le sous repertoire destination tmp/Init a ete cree pendant le formatage du workspace*/
		 /* remarque 2: la liste des fichiers contenus dans $dstage_ini_files (extrait du fichier ${SBM_Projet}_REF_INI.xml ) sont de la forme init\file.ini */
		 logger("INFO","Traitement du fichier INI:tmp${fileSep}${iniFile}","$VAR_DSTAGE_LogFileName")
		 
		 // Recopie du fichier INI dans un tmp
		 def ant = new AntBuilder()
		 try {
				 ant.copy(verbose: "true",file:"${sourceTarget}", tofile: "${destTarget}", overwrite:'true',includeEmptyDirs:'true')
		 }
		 catch (Exception e) {
			 logger("ERROR","cp -f ${sourceTarget} ${destTarget} failed... \n ${e.message}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 
		 // instantiation d'une instance Wini pointant sur le fichier INI ${destTarget} permettant la manipulation des donnees via une api INI_WINDOWS
		 def winiFile = new Wini(new File("${destTarget}"))
		 
		 try{
			 logger("TRACE","Recuperation de la liste de section INI definies pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
			 println "DEBUG:query=/IniFiles/file[@name=\"${iniFile}\"]/section/@name"
			 println "projectRefIni=$projectRefIni"
			 
			 def Map mapSection=xpath_execute("/IniFiles/file[@name=\"${iniFile}\"]/section/@name",projectRefIni)
			 if (mapSection["errorMessage"] != ""){
				 logger("ERROR", "Impossible de recuperer la liste de section INI definies pour le fichier $iniFile  dans le fichier ${projectRefIni}","$VAR_DSTAGE_LogFileName")
				 logger("ERROR",mapSection["errorMessage"],"$VAR_DSTAGE_LogFileName")
				 System.exit(1)
			 }
			 def Dstage_ini_Sections=mapSection["allElements"]
			 logger("TRACE","Dstage_ini_Sections="+Dstage_ini_Sections,"$VAR_DSTAGE_LogFileName")
			 
			/* For each $Dstage_Ini_Section in $Dstage_ini_Sections
			 xmlfile=$VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/$Projet_REF_INI.xml
			 xmlquery=/InitFiles/file[@name="$Dstage_IniFile"]/section[@name="$Dstage_Ini_Section"]/key/@name
			 Output=$Dstage_ini_Keys*/
			 
			 Dstage_ini_Sections.each{ section->
				 section=section.trim()
				 logger("TRACE","Recuperation de la liste de key definies pour la section $section pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
				 def Map mapkey=xpath_execute("/IniFiles/file[@name=\"${iniFile}\"]/section[@name=\"$section\"]/key/@name",projectRefIni)
				 if (mapkey["errorMessage"] != ""){
					 logger("ERROR", "Impossible de recuperer la liste de key definies pour la section $section pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
					 logger("ERROR",mapkey["errorMessage"],"$VAR_DSTAGE_LogFileName")
					 System.exit(1)
				 }
				 
				 def Dstage_ini_Keys=mapkey["allElements"]
				 logger("TRACE","Dstage_ini_Keys="+Dstage_ini_Keys,"$VAR_DSTAGE_LogFileName")
				 
				 /*
				  *For each $Dstage_IniKey in $Dstage_ini_Keys
							xmlfile=$VAR_DSTAGE_Workspace/pkg/$Dstage_SubDirectory/$Projet_REF_INI.xml
							xmlquery=/InitFiles/file[@name="$Dstage_IniFile"]/section[@name="$Dstage_Ini_Section"]/key[@name="$Dstage_IniKey"]/env[@name"Dstage_Environnement"]/@value
							Output=$Dstage_ini_Value
						
				  */
				 
				 /***************
				  * ??? default value dans le XML a revoir
				  *
				  */////////////
				  
				 Dstage_ini_Keys.each { key->
					 key=key.trim()
					 def String value
					 logger("TRACE","Recuperation de la valeur associee a la  key $key definies pour la section $section pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
					 logger("TRACE","query: /IniFiles/file[@name=\"${iniFile}\"]/section[@name=\"${section}\"]/key[@name=\"${key}\"]/env[@name=\"${VAR_Environnement}\"]/@value","$VAR_DSTAGE_LogFileName")
					 def Map mapvalue=xpath_execute("/IniFiles/file[@name=\"${iniFile}\"]/section[@name=\"${section}\"]/key[@name=\"${key}\"]/env[@name=\"${VAR_Environnement}\"]/@value",projectRefIni)
					 if (mapvalue["count"] == 0){
						 logger("TRACE","Recuperation de la valeur associee a la  key $key definies pour la section $section pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
						 logger("TRACE","query: /IniFiles/file[@name=\"${iniFile}\"]/section[@name=\"${section}\"]/key[@name=\"${key}\"]/@default","$VAR_DSTAGE_LogFileName")
						 mapvalue=xpath_execute("/IniFiles/file[@name=\"${iniFile}\"]/section[@name=\"${section}\"]/key[@name=\"${key}\"]/@default",projectRefIni)
						 if (mapvalue["count"] == 0){
							 logger("ERROR", "Impossible de recuperer la valeur associee a la  key $key definies pour la section $section pour le fichier $iniFile  dans le fichier ${SBM_Projet}_REF_INI.xml","$VAR_DSTAGE_LogFileName")
							 logger("ERROR",mapvalue["errorMessage"],"$VAR_DSTAGE_LogFileName")
							 System.exit(1)
						 }
						 else{
							 value=mapvalue["firstElement"]
						 }
					 }
					 else{
						  value=mapvalue["firstElement"]
					 }
					 
					 
					 logger("TRACE","Valorisation pour tmp${fileSep}${iniFile} : section=$section, key=$key, value=$value","$VAR_DSTAGE_LogFileName")
					 winiFile.put(section, key, value)
					 
				 }
			 }
			 
			 // Application des mise a jours dans le fichier INI
			 logger("TRACE","Application du parametrage pour tmp${fileSep}${iniFile}","$VAR_DSTAGE_LogFileName")
			 winiFile.store()
		 }
		 catch (Exception e) {
			 logger("ERROR", "Error durant l'application du parametrage du fichier tmp${fileSep}${iniFile}:\n ${e.message}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 
		 // Purge des espace entre le signe '=' (seperateur du couple (token,valeur). ex token = value => token=value
		 logger("TRACE", "Purge des espace entre le signe '=' pour tmp${fileSep}${iniFile}","$VAR_DSTAGE_LogFileName")
		 try{
		 
			 def String iniData = new File("${destTarget}").text
			 logger("TRACE", "avant purge\n^${iniData}","$VAR_DSTAGE_LogFileName")
			 def String iniDataNew=iniData.replaceAll(" *= *", "=")
			 def destDataInputStream=new File("${destTarget}").newDataOutputStream()
			 destDataInputStream << iniDataNew
			 destDataInputStream.close()
			 iniData = new File("${destTarget}").text
			 logger("TRACE", "apres purge\n^${iniData}","$VAR_DSTAGE_LogFileName")
		 }
		 catch(Exception e){
			 logger("ERROR", "Error durant la substitution de ' *= *' par '=' sur le fichier ${destTarget}:\n ${e.message}","$VAR_DSTAGE_LogFileName")
			 System.exit(1)
		 }
		 
		 
		 // Deploiement du fichier INI
		 logger("TRACE", "Purge des espace entre le signe '=' dans le fichier tmp${fileSep}${iniFile}","$VAR_DSTAGE_LogFileName")
		 sourceTarget1="${destTarget}"
		 destTarget2="${VAR_DSTAGE_Fichiers_Init}"
		 bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
		 cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
		 if(cr!= 0) { System.exit(1)}
		 
		 // Fin de traitement du fichier INI
		 
		 
	 } // Fin de traitement de l'ensemble des fichiers INI
	 
}
 

 
 
 
 /*
  *
  * STEP
  */
 
 // Deploiement des donnees a distribuer sur les differents serveurs
logger("INFO","\n------------\nDeploiement des donnees a distribuer sur les differents serveurs\n------------\n","$VAR_DSTAGE_LogFileName")
 
VAR_DSTAGE_TargetServerList.split("\\|").each{
	/**********************
	  * ???
	  * A CONFIRMER: en cas d'erreur on s'arret ou on continue
	  * A CONFIRMER en cas de non presence des sourceTarget on continue ou on arrete
	  * 
	  *************************/
	def String targetServerInst=it.trim()
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}03-ArboDisques-S${fileSep}fichiers_hashcodes"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_Fichiers_Hashcode1}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}03-ArboDisques-S${fileSep}fichiers_temporaires"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_Fichiers_Instance_Temporaires1}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}05-ArboDisques-M${fileSep}Compte Rendu"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_Compte_Rendu1}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}05-ArboDisques-M${fileSep}Master"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_Master1}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}05-ArboDisques-M${fileSep}TR"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_TR}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	sourceTarget="${VAR_DSTAGE_Workspace}${fileSep}pkg${fileSep}${pkgParentSubDirectory}${fileSep}05-ArboDisques-M${fileSep}Validation"
	destTarget="\\\\VIT-INF-P053\\Simulation-NAS\\${targetServerInst}${fileSep}${VAR_DSTAGE_Fichiers_Validation1}"
	bckTarget="${VAR_DSTAGE_Workspace}${fileSep}bck"
	cr=transfertToNas(sourceTarget,destTarget,bckTarget,VAR_DSTAGE_LogFileName )
	if(cr!= 0) { System.exit(1)}
	
	
	
}

// Generation d'un package de backup
logger("INFO","\n------------\nGeneration d'un package de backup tmp${fileSep}${SBM_BckPackage}.zip de bck${fileSep}\n------------\n","$VAR_DSTAGE_LogFileName")
def String zipName="${VAR_DSTAGE_Workspace}${fileSep}tmp${fileSep}${SBM_BckPackage}"
def String basedir="${VAR_DSTAGE_Workspace}"
try {
	ant = new AntBuilder()
	ant.zip(destfile: zipName, basedir:basedir,includes:"bck/**")
}
catch (Exception e) {
	logger("ERROR", "Error durant la creation du package de backup tmp${fileSep}${SBM_BckPackage}.zip:\n ${e.message}","$VAR_DSTAGE_LogFileName")
	System.exit(1)
}

/***
 * STEP SCP vers DIM SRV $VAR_DSTAGE_Workspace/tmp/$BckPackage.zip -> 10.66.11.14:/products/serena/depot/DATASTAGE/$SBM_Folder/$BckPackage.zip
 * 
 */
println "SCP vers DIM SRV $VAR_DSTAGE_Workspace/tmp/${SBM_BckPackage} -> 10.66.11.14:/products/serena/depot/DATASTAGE/${SBM_Folder}/$SBM_BckPackage}"
 
//-------------------------------------- Bloc Deploy SRA
