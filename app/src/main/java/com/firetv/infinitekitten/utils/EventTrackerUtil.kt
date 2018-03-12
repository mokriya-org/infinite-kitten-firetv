package com.firetv.infinitekitten.utils

import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.BuildConfig
import com.segment.analytics.Analytics
import com.segment.analytics.Properties

class EventTrackerUtil {
    companion object {

        private const val SEGMENT_API_KEY = "skrVmaWaEVpb5abNZYyPwhxIBVOdcIUc"

        const val EVENT_CAT_VIDEOS_BUTTON_SELECTED = "Selected Cat Videos"
        const val EVENT_HUMAN_VIDEOS_BUTTON_SELECTED = "Selected Human Videos"

        const val EVENT_VIDEO_WATCHED = "Finished Watching Video"

        fun setupTracker() {
            Analytics.setSingletonInstance(
                    Analytics.Builder(App.context, SEGMENT_API_KEY)
                            .trackApplicationLifecycleEvents()
                            .build())
        }

        fun trackEvent(eventName: String) {
            trackEvent(eventName, null)
        }

        fun trackEvent(eventName: String, properties: Properties?) {
            if (!BuildConfig.DEBUG) {
                Analytics.with(App.context).track(eventName, properties)
            }
        }
    }
}