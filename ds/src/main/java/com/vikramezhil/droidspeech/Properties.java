package com.vikramezhil.droidspeech;

import java.util.ArrayList;
import java.util.List;

/**
 * Droid Speech Properties
 *
 * @author Vikram Ezhil
 */

class Properties 
{
    List<String> supportedSpeechLanguages = new ArrayList<>();

    String currentSpeechLanguage;
    
    String listeningMsg;

    String oneStepVerifySpeechResult;

    long startListeningTime;
    
    long pauseAndSpeakTime;
    
    boolean offlineSpeechRecognition = false;
    
    boolean continuousSpeechRecognition = true;
    
    boolean showRecognitionProgressView = false;
    
    boolean oneStepResultVerify = false;
    
    boolean onReadyForSpeech = false;
    
    boolean speechResultFound = false;
    
    boolean closedByUser = false;
}
