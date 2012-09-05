// Copyright 2011 Google Inc. All Rights Reserved.

package com.tapit.mediation.admob;

import com.google.ads.mediation.MediationServerParameters;

/**
 * Settings for TapIt! from mediation backend
 */
public class AdMobAdapterServerParameters extends MediationServerParameters {
    /*
     * This class can either override load(Map<String, String>) or can provide
     * String fields with an @Parameter annotation. Optional parameters can be
     * specified in the annotation with required = false. If any required
     * parameters are missing from the server, this adapter will be skipped.
     */

    /**
     * TapIt! PublisherId.
     */
    @Parameter(name = "zoneid")
    public String zoneId;

}
