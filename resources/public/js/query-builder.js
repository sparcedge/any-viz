$(function() {
    $("#go-btn").click(function() {
        renderGraph();
    });
});

var buildQuery = function(db, coll) {
    var matches = getMatches();
    var groups = getGroups();
    var reducers = getReducers();
    var query = makeQuery(db, coll, matches, groups, reducers);
    return JSON.stringify(query);
}

var getTimeString = function(timeValue) {
    if(timeValue === "year") {
        return "YYYY";
    } else if(timeValue === "month") {
        return "YYYYMM";
    } else if(timeValue === "day") {
        return "YYYYMMDD";
    } else if(timeValue === "hour") {
        return "YYYYMMDDhh";
    } else if(timeValue === "minute") {
        return "YYYYMMDDhhmm";
    }
}

var selected = function(el) {
    return el.val();
}

var timeValue = function() {
    return selected($("#group-times"));
}

var first = function(obj) {
    for(var key in obj) {
        return obj[key];
    }
}

var createTimeData = function() {
    return function(timeResults) {
        var res = timeResults.data[0].data[0];
        return [
            moment(timeResults.group, getTimeString(timeValue())).unix()*1000,
            first(res)
        ];
    };
};

var createGroupedData = function() {
    return function(results) {
        return {
            name: results.group,
            data: results.data.map(createTimeData())
        };
    };
};

var updateQueryBlock = function(query) {
    var json = JSON.stringify(query, undefined, 2)
    $("#query").text(json);
};

var updateResultsBlock = function(results) {
    var json = JSON.stringify(results, undefined, 2)
    $("#results").text(json);
};

var renderGraph = function() {
    var pathArray = window.location.pathname.split('/')
        db = pathArray[2]
        coll = pathArray[3]
        query = buildQuery(db, coll);

    $.getJSON('/query?q='+encodeURIComponent(query), function(res) {  
        console.log(res);      
        var series;
        var data = res.results;
        var query = res.query;

        updateQueryBlock(query);
        updateResultsBlock(data);

        if(selected($("#group-entities")) === "none") {
            series = [{
                name: "Results", 
                data: data.map(createTimeData())
            }];
        } else {
            series = data.map(createGroupedData());
        }
        
        console.log(series);

        $('#dynamic-graph').highcharts({
            chart: {
                zoomType: 'x',
                type: 'spline'
            },
            title: {
                text: 'Query Results'
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Results'
                }
            },
            series: series
        });
    });
}

var matchOp = function() {
    var op = selected($("#match-ops"));
    if(op === "=") {
        return "eq";
    } else if(op === ">") {
        return "gt";
    } else if(op === ">=") {
        return "gte";
    } else if(op === "<") {
        return "lt";
    } else if(op === "<=") {
        return "lte";
    } else if(op === "!=") {
        return "ne";
    }
};

var getMatches = function() {
    var entity = selected($("#match-entities"))
    if(entity === "none") {
        return [];
    } else {
        return [createMatch(entity, matchOp(), $("#match-val").val())];
    }
};

var getGroups = function() {
    var entity = selected($("#group-entities"));
    var time = selected($("#group-times"))
    var durationGroup = createDurationGroup(time);
    var groups = [durationGroup];

    if(entity !== "none") {
        groups.push(createSegmentGroup(entity));
    }

    return groups;
};

var getReducers = function() {
    var entity = selected($("#reduce-entities"));
    var op = selected($("#reduce-ops"));
    return [createReducer(entity, op)];
};