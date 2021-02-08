package org.mtransit.parser.us_snohomish_county_community_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.StringUtils;
import org.mtransit.parser.ColorUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

// https://www.soundtransit.org/help-contacts/business-information/open-transit-data-otd
// https://www.soundtransit.org/help-contacts/business-information/open-transit-data-otd/otd-downloads
// http://developer.onebusaway.org/tmp/sound/gtfs/modified/29_gtfs.zip
// http://developer.onebusaway.org/tmp/sound/gtfs/modified_staged/29_gtfs.zip
// https://www.communitytransit.org/OpenData
// https://www.communitytransit.org/docs/default-source/open-data/gtfs/current.zip
public class SnohomishCountyCommunityTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../app-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new SnohomishCountyCommunityTransitBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating Community Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating Community Transit bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public boolean excludeRoute(@NotNull GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final String AGENCY_COLOR_BLUE = "1476C6"; // BLUE (from PDF schedule)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE;

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		//noinspection deprecation
		return Long.parseLong(CleanUtils.cleanMergedID(gRoute.getRouteId()));
	}

	private static final String SWIFT_COLOR = "2DA343"; // GREEN (from PDF map)
	private static final String LOCAL_ROUTES_COLOR = null; // AGENCY COLOR // BLUE (from PDF map)
	private static final String COMMUTER_ROUTES_COLOR = "F6861F"; // ORANGE (from PDF map)
	private static final String ST_EXPRESS_ROUTES_COLOR = "8D8687"; // GRAY (from PDF map)

	@SuppressWarnings("DuplicateBranchesInSwitch")
	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor()) //
				|| ColorUtils.WHITE.equalsIgnoreCase(gRoute.getRouteColor())) {
			//noinspection deprecation
			int rsn = Integer.parseInt(gRoute.getRouteId());
			switch (rsn) {
			// @formatter:off
			case 101: return LOCAL_ROUTES_COLOR;
			case 105: return LOCAL_ROUTES_COLOR;
			case 106: return LOCAL_ROUTES_COLOR;
			case 109: return LOCAL_ROUTES_COLOR;
			case 111: return LOCAL_ROUTES_COLOR;
			case 112: return LOCAL_ROUTES_COLOR;
			case 113: return LOCAL_ROUTES_COLOR;
			case 115: return LOCAL_ROUTES_COLOR;
			case 116: return LOCAL_ROUTES_COLOR;
			case 119: return LOCAL_ROUTES_COLOR;
			case 120: return LOCAL_ROUTES_COLOR;
			case 130: return LOCAL_ROUTES_COLOR;
			case 196: return LOCAL_ROUTES_COLOR;
			case 201: return LOCAL_ROUTES_COLOR;
			case 202: return LOCAL_ROUTES_COLOR;
			case 209: return LOCAL_ROUTES_COLOR;
			case 220: return LOCAL_ROUTES_COLOR;
			case 222: return LOCAL_ROUTES_COLOR;
			case 227: return LOCAL_ROUTES_COLOR;
			case 230: return LOCAL_ROUTES_COLOR;
			case 240: return LOCAL_ROUTES_COLOR;
			case 247: return LOCAL_ROUTES_COLOR;
			case 270: return LOCAL_ROUTES_COLOR;
			case 271: return LOCAL_ROUTES_COLOR;
			case 277: return LOCAL_ROUTES_COLOR;
			case 280: return LOCAL_ROUTES_COLOR;
			case 402: return COMMUTER_ROUTES_COLOR;
			case 405: return COMMUTER_ROUTES_COLOR;
			case 410: return COMMUTER_ROUTES_COLOR;
			case 412: return COMMUTER_ROUTES_COLOR;
			case 413: return COMMUTER_ROUTES_COLOR;
			case 415: return COMMUTER_ROUTES_COLOR;
			case 416: return COMMUTER_ROUTES_COLOR;
			case 417: return COMMUTER_ROUTES_COLOR;
			case 421: return COMMUTER_ROUTES_COLOR;
			case 422: return COMMUTER_ROUTES_COLOR;
			case 424: return COMMUTER_ROUTES_COLOR;
			case 425: return COMMUTER_ROUTES_COLOR;
			case 435: return COMMUTER_ROUTES_COLOR;
			case 510: return ST_EXPRESS_ROUTES_COLOR;
			case 511: return ST_EXPRESS_ROUTES_COLOR;
			case 512: return ST_EXPRESS_ROUTES_COLOR;
			case 513: return ST_EXPRESS_ROUTES_COLOR;
			case 532: return ST_EXPRESS_ROUTES_COLOR;
			case 535: return ST_EXPRESS_ROUTES_COLOR;
			case 701: return SWIFT_COLOR;
			case 810: return COMMUTER_ROUTES_COLOR;
			case 821: return COMMUTER_ROUTES_COLOR;
			case 855: return COMMUTER_ROUTES_COLOR;
			case 860: return COMMUTER_ROUTES_COLOR;
			case 871: return COMMUTER_ROUTES_COLOR;
			case 880: return COMMUTER_ROUTES_COLOR;
			// @formatter:on
			}
			throw new MTLog.Fatal("Unexpected route color %s!", gRoute);
		}
		return super.getRouteColor(gRoute);
	}

	@NotNull
	@Override
	public String cleanStopOriginalId(@NotNull String gStopId) {
		gStopId = CleanUtils.cleanMergedID(gStopId);
		return gStopId;
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		mTrip.setHeadsignString(
				cleanTripHeadsign(gTrip.getTripHeadsignOrDefault()),
				gTrip.getDirectionIdOrDefault()
		);
	}

	private static final String _SLASH_ = " / ";
	private static final String _AND_ = " & ";
	private static final String PARK_AND_RIDE_SHORT = "P&R";

	private static final String MARINER_PARK_AND_RIDE = "Mariner " + PARK_AND_RIDE_SHORT;

	private static final String AURORA_VILLAGE = "Aurora Vlg";
	private static final String AURORA_VILLAGE_STATION = AURORA_VILLAGE + " Sta";
	private static final String ARLINGTON = "Arlington";
	private static final String BOEING = "Boeing";
	private static final String DARRINGTON = "Darrington";
	private static final String EVERETT = "Everett";
	private static final String EVERETT_BOEING = EVERETT + " Boeing";
	private static final String GOLD_BAR = "Gold Bar";
	private static final String HARDESON = "Hardeson";
	private static final String HARDESON_ROAD = HARDESON + " Rd";
	private static final String LYNNWOOD = "Lynnwood";
	private static final String MARYSVILLE = "Marysville";
	private static final String MC_COLLUM_PARK = "McCollum Pk";
	private static final String MC_COLLUM_PARK_PARK_AND_RIDE = MC_COLLUM_PARK + " " + PARK_AND_RIDE_SHORT;
	private static final String MONROE = "Monroe";

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 105L) {
			if (Arrays.asList( //
					MARINER_PARK_AND_RIDE, //
					HARDESON //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HARDESON, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					MARINER_PARK_AND_RIDE, //
					HARDESON_ROAD //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HARDESON_ROAD, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 230L) {
			if (Arrays.asList( //
					ARLINGTON, //
					DARRINGTON //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(DARRINGTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 270L) {
			if (Arrays.asList( //
					BOEING, //
					"Seaway TC", //
					EVERETT //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EVERETT, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					MONROE, //
					GOLD_BAR //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(GOLD_BAR, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 271L) {
			if (Arrays.asList( //
					EVERETT + _SLASH_ + BOEING, //
					EVERETT //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EVERETT, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					MONROE, //
					MONROE + _SLASH_ + GOLD_BAR, //
					GOLD_BAR //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(GOLD_BAR, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 280L) {
			if (Arrays.asList( //
					BOEING, //
					EVERETT + _SLASH_ + BOEING, //
					"Seaway TC", //
					EVERETT //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EVERETT, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					"Lk Stevens", //
					"Granite Falls" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString("Granite Falls", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 535L) {
			if (Arrays.asList( //
					LYNNWOOD + _AND_ + EVERETT, //
					LYNNWOOD //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(LYNNWOOD, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 701L) {
			if (Arrays.asList( //
					AURORA_VILLAGE, //
					AURORA_VILLAGE_STATION //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(AURORA_VILLAGE_STATION, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 810L) {
			if (Arrays.asList( //
					LYNNWOOD, //
					MC_COLLUM_PARK_PARK_AND_RIDE //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MC_COLLUM_PARK_PARK_AND_RIDE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 821L) {
			if (Arrays.asList( //
					LYNNWOOD, //
					MARYSVILLE //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MARYSVILLE, mTrip.getHeadsignId());
				return true;
			}
		}
		throw new MTLog.Fatal("Unexpected trips to merge: %s & %s!", mTrip, mTripToMerge);
	}

	private static final Pattern EVERETT_EVERETT_BOEING = Pattern.compile("((^|\\W)(everett / everett boeing)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String EVERETT_EVERETT_BOEING_REPLACEMENT = "$2" + EVERETT_BOEING + "$4";

	private static final Pattern SWIFT_STATION = Pattern.compile("((^|\\W)(swift station|swift sta)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String SWIFT_STATION_REPLACEMENT = "$2" + "Sta" + "$4";

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.keepToAndRemoveVia(tripHeadsign);
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = EVERETT_EVERETT_BOEING.matcher(tripHeadsign).replaceAll(EVERETT_EVERETT_BOEING_REPLACEMENT);
		tripHeadsign = SWIFT_STATION.matcher(tripHeadsign).replaceAll(SWIFT_STATION_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.SAINT.matcher(gStopName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection deprecation
		String stopId = gStop.getStopId();
		if (stopId != null && stopId.length() > 0) {
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
