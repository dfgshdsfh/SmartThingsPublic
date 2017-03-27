/**
 *  Night Humidity
 *
 *  Copyright 2016 Clayton Nummer
 *
 */
definition(
    name: "Humidity Maintainer",
    namespace: "claytonjn",
    author: "Clayton Nummer",
    description: "Controls a Humidifier to maintain relative humidity level.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
        	paragraph "Controls a Humidifier to maintain relative humidity level."
            input "humiditys", "capability.relativeHumidityMeasurement", title: "Humidy Sensor(s)", multiple: true
            input "humidifiers", "capability.switch", title: "Humidifier(s)", multiple: true
            input "relativeHumidity", "number", title: "Relative Humidity to Maintain", defaultValue: 50, required: true
            mode(title: "Set for specific mode(s)")
            label title: "Assign a name", required: false
        }
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(settings.humiditys, "humidity", evtHandler)
    subscribe(location, "mode", evtHandler)
}

void evtHandler(evt) {
    def avgHumidity = 0
    for (humidity in settings.humiditys) {
        avgHumidity += humidity.currentValue("humidity")
    }
    avgHumidity = avgHumidity / settings.humiditys.size()
    if (avgHumidity < settings.relativeHumidity) {
        settings.humidifiers?.on()
    } else {
        settings.humidifiers?.off()
    }
}