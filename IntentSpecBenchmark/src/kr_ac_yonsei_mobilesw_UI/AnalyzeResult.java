package kr_ac_yonsei_mobilesw_UI;

public enum AnalyzeResult {
	Normal, Exit, ErrorExit, IntentSpecCatch, 
	IntentSpecPassAndNormal, IntentSpecPassAndExit, IntentSpecPassAndErrorExit,
	CantAnalyze;
}
