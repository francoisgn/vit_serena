//chargement des libs
${p:lib_logger}

// pas de $LogFileName - le fichier peut ne pas exister
fileSep=File.separator

// input SBM
logger("TRACE","Chargement des inputs SBM")
outProps.setProperty("Var_SBM_Nom_Log","${p:SBM_Nom_Log}")
outProps.setProperty("Var_SBM_BckPackage","${p:SBM_BckPackage}")
outProps.setProperty("Var_SBM_Projet","${p:SBM_Projet}")
outProps.setProperty("Var_SBM_Password","${p:SBM_Password}")
outProps.setProperty("Var_SBM_Folder","${p:SBM_Folder}")
outProps.setProperty("Var_SBM_Package","${p:SBM_Package}")
outProps.setProperty("Var_SBM_BaselineId","${p:SBM_BaselineId}")
outProps.setProperty("Var_SBM_Login","${p:SBM_Login}")
outProps.setProperty("Var_SBM_Target_Server","${p:SBM_Target_Server}")
outProps.setProperty("Var_SBM_Target_Server2","${p:SBM_Target_Server2}")
outProps.setProperty("Var_SBM_Target_Server3","${p:SBM_Target_Server3}")
outProps.setProperty("Var_SBM_Environnement","${p:SBM_Environnement}")



//  Env. Cst
logger("TRACE","Chargement constantes d'environnement")
outProps.setProperty("Var_CST_DSTAGE_tmp_directories","${p:environment/CST_DSTAGE_tmp_directories}")
outProps.setProperty("Var_CST_DSTAGE_Root_Directory","${p:environment/CST_DSTAGE_Root_Directory}")
outProps.setProperty("Var_CST_DSTAGE_ArboDisque_Files","${p:environment/CST_DSTAGE_ArboDisque_Files}")
outProps.setProperty("Var_CST_DSTAGE_NAS_Folders","${p:environment/CST_DSTAGE_NAS_Folders}")
outProps.setProperty("Var_CST_DSTAGE_M_Folders","${p:environment/CST_DSTAGE_M_Folders}")
outProps.setProperty("Var_CST_DSTAGE_IsTool_Path","${p:environment/CST_DSTAGE_IsTool_Path}")
outProps.setProperty("Var_CST_DSTAGE_Port_Domaine","${p:environment/CST_DSTAGE_Port_Domaine}")
outProps.setProperty("Var_CST_DIM_SASDepot","${p:environment/CST_DIM_SASDepot}")
outProps.setProperty("Var_CST_Nas_Projet_Path","${p:environment/CST_Nas_Projet_Path}")
outProps.setProperty("Var_CST_DSTAGE_Environnement","${p:environment/CST_DSTAGE_Environnement}")


// VARIABLES
logger("TRACE","Chargement variables")
// definition des variables necessaires au calcul des OutProps
def VarL_DSTAGE_Fichiers_Manuels_Directory="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Fichiers_manuels"
def VarL_DSTAGE_Fichiers_Sortie="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Fichiers_sortie"
def VarL_DSTAGE_Fichiers_Temporaires="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Fichiers_temporaires"
def VarL_DSTAGE_Fichiers_Stockage="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Stockage"
def VarL_DSTAGE_Fichiers_Sql="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}SQL"
def VarL_DSTAGE_Fichiers_Init="${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Init"
def VarL_DSTAGE_Fichiers_Master="M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Master"
def VarL_DSTAGE_Fichiers_TR="M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}TR}"
def VarL_DSTAGE_Fichiers_Validation="M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Validation"
def VarL_DSTAGE_Workspace="${p:environment/CST_DSTAGE_Root_Directory}${fileSep}${p:SBM_BaselineId}"

// variable en OutProps
outProps.setProperty("VAR_DSTAGE_Fichiers_Manuels_Directory","${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Fichiers_manuels")
outProps.setProperty("VAR_DSTAGE_Fichiers_Sortie","${p:environment/CST_Nas_Projet_Path}${p:SBM_Projet}${fileSep}Fichiers_sortie")
outProps.setProperty("VAR_DSTAGE_Fichiers_Temporaires","${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Fichiers_temporaires")
outProps.setProperty("VAR_DSTAGE_Fichiers_Stockage","${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Stockage")
outProps.setProperty("VAR_DSTAGE_Fichiers_Sql","${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}SQL")
outProps.setProperty("VAR_DSTAGE_Fichiers_Init","${p:environment/CST_Nas_Projet_Path}${fileSep}${p:SBM_Projet}${fileSep}Init")
outProps.setProperty("VAR_DSTAGE_Fichiers_Hashcode1","DS${fileSep}${p:SBM_Projet}${fileSep}fichiers_hashcodes")
outProps.setProperty("VAR_DSTAGE_Fichiers_Instance_Temporaires1","DS${fileSep}${p:SBM_Projet}${fileSep}fichiers_temporaires")
outProps.setProperty("VAR_DSTAGE_Compte_Rendu1","Masters${fileSep}${p:SBM_Projet}${fileSep}Compte Rendu")
outProps.setProperty("VAR_DSTAGE_Master1","Masters${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Master")
outProps.setProperty("VAR_DSTAGE_TR","Masters${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}TR")
outProps.setProperty("VAR_DSTAGE_Fichiers_Validation1","Masters${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Validation")
outProps.setProperty("VAR_DSTAGE_backupfull_file","backupfull_${p:SBM_BckPackage}.txt")
outProps.setProperty("VAR_DSTAGE_Nas_Target_Folder","${VarL_DSTAGE_Fichiers_Manuels_Directory}|${VarL_DSTAGE_Fichiers_Sortie}|${VarL_DSTAGE_Fichiers_Temporaires}|${VarL_DSTAGE_Fichiers_Init}|${VarL_DSTAGE_Fichiers_Sql}|${VarL_DSTAGE_Fichiers_Stockage}")
outProps.setProperty("VAR_DSTAGE_Fichiers_Master","M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Master")
outProps.setProperty("VAR_DSTAGE_Fichiers_TR","M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}TR")
outProps.setProperty("VAR_DSTAGE_Fichiers_Validation","M:${fileSep}MASTERS${fileSep}${p:SBM_Projet}${fileSep}Suivi${fileSep}Validation")
outProps.setProperty("VAR_DSTAGE_M_Target_Folder","${VarL_DSTAGE_Fichiers_Master}|${VarL_DSTAGE_Fichiers_TR}|${VarL_DSTAGE_Fichiers_Validation}")
outProps.setProperty("VAR_DSTAGE_User","${p:SBM_Login}")
outProps.setProperty("VAR_DSTAGE_Password","${p:SBM_Password}")
outProps.setProperty("VAR_DSTAGE_Workspace","${p:environment/CST_DSTAGE_Root_Directory}${fileSep}${p:SBM_BaselineId}")
outProps.setProperty("VAR_DSTAGE_LogFileName","${VarL_DSTAGE_Workspace}${fileSep}log${fileSep}${p:SBM_Nom_Log}")
	
// liste des serveurs cibles
// Combien de SBM_Target_Server necessaire?
logger("INFO","Calcul dynamique de la liste des Targets serveurs")
def VarL_DSTAGE_TargetServerTab=[]
if("${p:SBM_Target_Server}" != ""){VarL_DSTAGE_TargetServerTab.add("${p:SBM_Target_Server}".trim())
	logger("INFO","Targets serveur:${p:SBM_Target_Server} ")
	
}
if("${p:SBM_Target_Server2}" != ""){VarL_DSTAGE_TargetServerTab.add("${p:SBM_Target_Server2}".trim())
	logger("INFO","Targets serveur:${p:SBM_Target_Server2} ")
	
}
if("${p:SBM_Target_Server3}" != ""){VarL_DSTAGE_TargetServerTab.add("${p:SBM_Target_Server3}".trim())
	logger("INFO","Targets serveur:${p:SBM_Target_Server3} ")
}

def VarL_DSTAGE_TargetServerList=""
VarL_DSTAGE_TargetServerTab.each{
	if(VarL_DSTAGE_TargetServerList == ""){ VarL_DSTAGE_TargetServerList=it.trim()}else{VarL_DSTAGE_TargetServerList=VarL_DSTAGE_TargetServerList+'|'+it.trim()}
}

// out Properties target server list
outProps.setProperty("VAR_DSTAGE_TargetServerList","${VarL_DSTAGE_TargetServerList}")






