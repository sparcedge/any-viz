$(function() {
    $("#go-btn").click(function() {
        renderGraph();
    });

    $("#clear-btn").click(function() {
        $('#dynamic-graph').highcharts().destroy();
        initGraphPage();
    });

    initGraphPage();
});

var initGraphPage = function() {
    // initial/default chart options
    $('#dynamic-graph').highcharts({
        chart: {
            zoomType: 'x'
        },
        title: {
            text: null
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: "Results"
            }
        }
    });

    hideGraphTabs(true);
}

var hideGraphTabs = function(hide) {
    if(hide){
        $('#dynamic-graph').hide();
        $("#query-tabs").hide();
        $("#clear-btn").hide();        
    } else {
        $('#dynamic-graph').show();
        $("#query-tabs").show();
        $("#clear-btn").show();
    }
}

var buildQuery = function(db, coll) {
    var start = getStartDate();
    var matches = getMatches();
    var groups = getGroups();
    var reducers = getReducers();
    var query = makeQuery(db, coll, start, matches, groups, reducers);
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

var updateBlock = function(content, selector) {
    var json = JSON.stringify(content, undefined, 2);
    $(selector).html(json);
};

var updateQueryBlock = function(query) {
    updateBlock(query, '#query');
};

var updateResultsBlock = function(results) {
    updateBlock(results, '#results');
};

var renderGraph = function() {
    var pathArray = window.location.pathname.split('/')
    db = pathArray[2]
    coll = pathArray[3]
    query = buildQuery(db, coll);

    $.getJSON('/query?q='+encodeURIComponent(query), function(res) {  

        var series;
        var data = res.results;
        var query = res.query;

        updateQueryBlock(query);
        updateResultsBlock(data);

        var chart = $('#dynamic-graph').highcharts();

        if(selected($("#group-entities")) === "none") {
            series = [{
                name: selected($("#reduce-entities")) + ":" + selected($("#reduce-ops")) + "/" + selected($("#group-times")), 
                type: selected($("#graph-type")),
                data: data.map(createTimeData())
            }];
        } else {
            series = data.map(createGroupedData());
        }
         
        chart.addSeries(series[0]);

        hideGraphTabs(false);
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

var getStartDate = function() {
    var daterange = selected($("#date-period"));
    var startdate;
    var days;

    if(daterange === "last hour") {
        startdate = moment().subtract('hours', 1).unix() * 1000;
    } else if(daterange === "last 6 months") {
        startdate = moment().subtract('months', 6).unix() * 1000;
    } else {
        if(daterange === "last day"){
            days = 1;
        } else if(daterange === "last 7 days") {
            days = 7;
        } else if (daterange === "last 30 days") {
            days = 30;
        } else if(daterange === "last year") {
            days = 365;
        } else { // all time
            return ;
        }
        startdate = moment().subtract('days', days).unix() * 1000;
    }

    return startdate;
};