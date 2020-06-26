// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;   
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {

    /**
    * Returns a collection of available TimeRange given a collection of Event objects and
    * MeetingRequest. A TimeRange is available if the person in MeetingRequest are all available.
    * We first do this by checking which TimeRange are the attendees not available in and 
    * use sliding window to record the available TimeRange provided they satisfy the duration.
    * @param    events  a collection of Event objects
    * @param    request a MeetingRequest object
    * @return           collection of available TimeRange  
    */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        ArrayList<TimeRange> overlappingTime = new ArrayList<TimeRange>();
        ArrayList<TimeRange> mergedEventTime = new ArrayList<TimeRange>();
        ArrayList<TimeRange> availableTime = new ArrayList<TimeRange>();
        Set<String> requestAttendees = new HashSet<String>(request.getAttendees());

        // record TimeRange whereby any of attendees is unavailable, i.e. in another event
        for(Event event: events){
            Set<String> eventAttendees = new HashSet<String>(event.getAttendees());
            eventAttendees.retainAll(requestAttendees);
            if (!eventAttendees.isEmpty()) {
                overlappingTime.add(event.getWhen());
            }
        }

        // merge eventTime time range
        mergedEventTime = mergeEventTime(overlappingTime);

        // sliding window to find available timing that matches the duration
        int prevEnd = TimeRange.START_OF_DAY;
        long duration=request.getDuration();

        // check from START_OF_DAY and invertals within each event
        for(TimeRange curEvent: mergedEventTime){
            if (curEvent.start() - prevEnd >= duration){
                availableTime.add(TimeRange.fromStartEnd(prevEnd, curEvent.start(), false));
            }
            prevEnd = curEvent.end();
        }

        // check from last event to END_OF_DAY
        if (TimeRange.END_OF_DAY - prevEnd >= duration) {
            availableTime.add(TimeRange.fromStartEnd(prevEnd, TimeRange.END_OF_DAY, true));
        }

        return availableTime;
    }

    /**
    * This function will merge overlapping intervals. We do this by first sorting event time by 
    * their start time, and try to merge greedily as long as the next event time is still 
    * overlapping. 
    * @param    events  a collection of Event objects
    * @return           collection of merged event time, sorted by start
    */
    private ArrayList<TimeRange> mergeEventTime(ArrayList<TimeRange> eventTime){
        if (eventTime.size() < 1) {return eventTime;}

        Collections.sort(eventTime, TimeRange.ORDER_BY_START);
        ArrayList<TimeRange> mergedEventTime = new ArrayList();  
        int prevStart = eventTime.get(0).start(), prevEnd = eventTime.get(0).end();

        for (int curIndex = 0; curIndex < eventTime.size(); curIndex++){
            TimeRange curEvent = eventTime.get(curIndex);

            // merge greedily with previous event
            if (TimeRange.fromStartEnd(prevStart, prevEnd, false).overlaps(curEvent)) {
                prevEnd = Math.max(prevEnd, curEvent.end());
            }

            // no overlapping, we add to the result list and restart prevStart and prevEnd
            else {
                mergedEventTime.add(TimeRange.fromStartEnd(prevStart, prevEnd, false));
                prevStart = curEvent.start();
                prevEnd = curEvent.end();
            }
        }
        // note that the last event is not inserted, whether they are overlapping or not
        // so we need to add one more time
        mergedEventTime.add(TimeRange.fromStartEnd(prevStart, prevEnd, false));
        
        return mergedEventTime;
    }
}
