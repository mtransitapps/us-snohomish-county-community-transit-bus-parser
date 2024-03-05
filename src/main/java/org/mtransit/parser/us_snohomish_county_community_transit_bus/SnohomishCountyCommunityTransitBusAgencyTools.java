package org.mtransit.parser.us_snohomish_county_community_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.ColorUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.mt.data.MAgency;

import java.util.List;
import java.util.Locale;

// https://www.soundtransit.org/help-contacts/business-information/open-transit-data-otd
// https://www.soundtransit.org/help-contacts/business-information/open-transit-data-otd/otd-downloads
// http://developer.onebusaway.org/tmp/sound/gtfs/modified/29_gtfs.zip
// http://developer.onebusaway.org/tmp/sound/gtfs/modified_staged/29_gtfs.zip
// https://www.communitytransit.org/OpenData
// https://www.communitytransit.org/docs/default-source/open-data/gtfs/current.zip
// https://www.communitytransit.org/docs/default-source/open-data/gtfs/future.zip
public class SnohomishCountyCommunityTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new SnohomishCountyCommunityTransitBusAgencyTools().start(args);
	}

	@Nullable
	@Override
	public List<Locale> getSupportedLanguages() {
		return LANG_EN;
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	public String getAgencyName() {
		return "Community Transit";
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return true;
	}

	@Nullable
	@Override
	public Long convertRouteIdFromShortNameNotSupported(@NotNull String routeShortName) {
		switch (routeShortName) {
		case "Swift Blue":
			return 701L;
		case "Swift Green":
			return 702L;
		case "Swift Orange":
			return 703L;
		}
		return super.convertRouteIdFromShortNameNotSupported(routeShortName);
	}

	@Override
	public boolean defaultRouteLongNameEnabled() {
		return true;
	}

	// https://www.communitytransit.org/busplus
	// https://www.communitytransit.org/docs/default-source/mappdfs/21marchmaps/busplusbookpdf/bus-plus-march-2021-web.pdf?sfvrsn=f94a3bd4_4
	private static final String SWIFT_GREEN_COLOR = "36A535"; // GREEN (from PDF map)
	private static final String SWIFT_BLUE_COLOR = "1477C6"; // BLUE  (from PDF map)
	private static final String LOCAL_ROUTES_COLOR = "42B3B7"; // BLUE TEAL (from PDF map)
	private static final String COMMUTER_ROUTES_COLOR = "F3AA4F"; // ORANGE (from PDF map)
	private static final String ST_EXPRESS_ROUTES_COLOR = "B2B1B1"; // GRAY (from PDF map)

	@Nullable
	@Override
	public String fixColor(@Nullable String color) {
		if (ColorUtils.WHITE.equalsIgnoreCase(color)) {
			return null;
		}
		return super.fixColor(color);
	}

	@SuppressWarnings("DuplicateBranchesInSwitch")
	@Nullable
	@Override
	public String provideMissingRouteColor(@NotNull GRoute gRoute) {
		//noinspection deprecation
		switch (gRoute.getRouteShortName()) {
		case "101":
		case "105":
		case "106":
		case "107":
		case "109":
		case "111":
		case "112":
		case "113":
		case "115":
		case "116":
		case "119":
		case "120":
		case "130":
		case "196":
		case "201":
		case "202":
		case "209":
		case "220":
		case "222":
		case "227":
		case "230":
		case "240":
		case "247":
		case "270":
		case "271":
		case "277":
		case "280":
			return LOCAL_ROUTES_COLOR;
		case "402":
		case "405":
		case "410":
		case "412":
		case "413":
		case "415":
		case "416":
		case "417":
		case "421":
		case "422":
		case "424":
		case "425":
		case "435":
			return COMMUTER_ROUTES_COLOR;
		case "510":
		case "511":
		case "512":
		case "513":
		case "532":
		case "535":
			return ST_EXPRESS_ROUTES_COLOR;
		case "701":
		case "Swift Blue":
			return SWIFT_BLUE_COLOR;
		case "702":
		case "Swift Green":
			return SWIFT_GREEN_COLOR;
		case "810":
		case "821":
		case "855":
		case "860":
		case "871":
		case "880":
			return COMMUTER_ROUTES_COLOR;
		}
		throw new MTLog.Fatal("Unexpected route color %s!", gRoute.toStringPlus());
	}

	@NotNull
	@Override
	public String cleanStopOriginalId(@NotNull String gStopId) {
		gStopId = CleanUtils.cleanMergedID(gStopId);
		return gStopId;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.keepToAndRemoveVia(tripHeadsign);
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.toLowerCaseUpperCaseWords(getFirstLanguageNN(), gStopName, getIgnoredWords());
		gStopName = CleanUtils.cleanBounds(gStopName);
		gStopName = CleanUtils.SAINT.matcher(gStopName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@NotNull
	private String[] getIgnoredWords() {
		return new String[]{
				"NE", "NW",
				"SE", "SW",
		};
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection deprecation
		String stopId = gStop.getStopId();
		if (!stopId.isEmpty()) {
			if (CharUtils.isDigitsOnly(stopId)) {
				return Integer.parseInt(stopId);
			}
			stopId = CleanUtils.cleanMergedID(stopId);
			if (CharUtils.isDigitsOnly(stopId)) {
				return Integer.parseInt(stopId);
			}
		}
		throw new MTLog.Fatal("Unexpected stop ID for " + gStop + " !");
	}
}
