package com.vikramezhil.droidspeech;

import java.util.List;

/**
 * Droid Speech Language Details Listener
 *
 * @author Vikram Ezhil
 */

interface OnLanguageDetailsListener
{
    /**
     * Sends an update with the device language details
     *
     * @param defaultLanguage The default language
     *
     * @param otherLanguages The other supported languages
     */
    void onLanguageDetailsInfo(String defaultLanguage, List<String> otherLanguages);
}
