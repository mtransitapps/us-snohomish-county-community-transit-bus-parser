package org.mtransit.parser.us_snohomish_county_community_transit_bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;
import org.mtransit.parser.mt.data.MTripStop;

// http://www.soundtransit.org/Developer-resources/Data-downloads
// http://developer.onebusaway.org/tmp/sound/gtfs/modified/29_gtfs.zip
// http://developer.onebusaway.org/tmp/sound/gtfs/modified_staged/29_gtfs.zip
public class SnohomishCountyCommunityTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/us-snohomish-county-community-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new SnohomishCountyCommunityTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("\nGenerating Community Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this, true);
		super.start(args);
		System.out.printf("\nGenerating Community Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public boolean excludeRoute(GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final String AGENCY_COLOR_BLUE = "1476C6"; // BLUE (from PDF schedule)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE;

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String SWIFT_COLOR = "2DA343"; // GREEN (from PDF map)
	private static final String LOCAL_ROUTES_COLOR = null; // AGENCY COLOR // BLUE (from PDF map)
	private static final String COMMUTER_ROUTES_COLOR = "F6861F"; // ORANGE (from PDF map)
	private static final String ST_EXPRESS_ROUTES_COLOR = "8D8687"; // GRAY (from PDF map)

	@Override
	public String getRouteColor(GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor()) //
				|| DefaultAgencyTools.WHITE.equalsIgnoreCase(gRoute.getRouteColor())) {
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
			if (isGoodEnoughAccepted()) {
				return null;
			}
			System.out.printf("\nUnexpected route color %s!\n", gRoute);
			System.exit(-1);
			return null;
		}
		return super.getRouteColor(gRoute);
	}

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), gTrip.getDirectionId());
	}

	private static final String SLASH = " / ";
	private static final String AND = " & ";
	private static final String PARK_AND_RIDE_SHORT = "P&R";

	private static final String MARINER_PARK_AND_RIDE = "Mariner " + PARK_AND_RIDE_SHORT;
	private static final String TRANSIT_CENTER_SHORT = "TC";

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
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
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
					EVERETT + SLASH + BOEING, //
					EVERETT //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EVERETT, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					MONROE, //
					MONROE + SLASH + GOLD_BAR, //
					GOLD_BAR //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(GOLD_BAR, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 280L) {
			if (Arrays.asList( //
					BOEING, //
					EVERETT + SLASH + BOEING, //
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
					LYNNWOOD + AND + EVERETT, //
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
		System.out.printf("\nUnexpected trips to merge: %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	private static final Pattern TO = Pattern.compile("((^|\\W){1}(to)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final Pattern VIA = Pattern.compile("((^|\\W){1}(via)(\\W|$){1})", Pattern.CASE_INSENSITIVE);

	private static final String COMMUNITY_COLLEGE_SHORT = "CC";
	private static final Pattern COMMUNITY_COLLEGE = Pattern.compile("((^|\\W){1}(community college)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String COMMUNITY_COLLEGE_REPLACEMENT = "$2" + COMMUNITY_COLLEGE_SHORT + "$4";

	private static final Pattern EVERETT_EVERETT_BOEING = Pattern.compile("((^|\\W){1}(everett \\/ everett boeing)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String EVERETT_EVERETT_BOEING_REPLACEMENT = "$2" + EVERETT_BOEING + "$4";

	private static final Pattern PARK_AND_RIDE = Pattern.compile("((^|\\W){1}(park \\& ride)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String PARK_AND_RIDE_REPLACEMENT = "$2" + PARK_AND_RIDE_SHORT + "$4";

	private static final Pattern SWIFT_STATION = Pattern.compile("((^|\\W){1}(swift station|swift sta)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String SWIFT_STATION_REPLACEMENT = "$2" + "Sta" + "$4";

	private static final Pattern TRANSIT_CENTER = Pattern.compile("((^|\\W){1}(transit center|transit|centre)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String TRANSIT_CENTER_REPLACEMENT = "$2" + TRANSIT_CENTER_SHORT + "$4";

	private static final String UNIVERSITY_DISTRICT_SHORT = "U District";
	private static final Pattern UNIVERSITY_DISTRICT = Pattern.compile("((^|\\W){1}(university district)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String UNIVERSITY_DISTRICT_REPLACEMENT = "$2" + UNIVERSITY_DISTRICT_SHORT + "$4";

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		Matcher matcherTO = TO.matcher(tripHeadsign);
		if (matcherTO.find()) {
			String gTripHeadsignAfterTO = tripHeadsign.substring(matcherTO.end());
			tripHeadsign = gTripHeadsignAfterTO;
		}
		Matcher matcherVIA = VIA.matcher(tripHeadsign);
		if (matcherVIA.find()) {
			String gTripHeadsignBeforeVIA = tripHeadsign.substring(0, matcherVIA.start());
			tripHeadsign = gTripHeadsignBeforeVIA;
		}
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = COMMUNITY_COLLEGE.matcher(tripHeadsign).replaceAll(COMMUNITY_COLLEGE_REPLACEMENT);
		tripHeadsign = EVERETT_EVERETT_BOEING.matcher(tripHeadsign).replaceAll(EVERETT_EVERETT_BOEING_REPLACEMENT);
		tripHeadsign = PARK_AND_RIDE.matcher(tripHeadsign).replaceAll(PARK_AND_RIDE_REPLACEMENT);
		tripHeadsign = TRANSIT_CENTER.matcher(tripHeadsign).replaceAll(TRANSIT_CENTER_REPLACEMENT);
		tripHeadsign = SWIFT_STATION.matcher(tripHeadsign).replaceAll(SWIFT_STATION_REPLACEMENT);
		tripHeadsign = UNIVERSITY_DISTRICT.matcher(tripHeadsign).replaceAll(UNIVERSITY_DISTRICT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = CleanUtils.SAINT.matcher(gStopName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.removePoints(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}
}
