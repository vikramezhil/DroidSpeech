package com.vikramezhil.droidspeech;

import java.util.List;

/**
 * Created by Vikram Ezhil
 *
 * Droid Speech Language Details Listener
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
