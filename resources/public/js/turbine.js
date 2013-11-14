var makeQuery = function(db, coll, start, matches, groups, reducers) {
    var json;

    if(start !== ""){
        json = {
            db: db,
            coll: coll,
            start: start,
            match: matches,
            group: groups,
            reduce: reducers
        }
    } else {
        json = {
            db: db,
            coll: coll,
            match: matches,
            group: groups,
            reduce: reducers
        } 
    }

    return json;
}

var createMatch = function(entity, op, value) {
    var matchOp = {};
    matchOp[op] = value;
    var match = {};
    match[entity] = matchOp;
    return matchOp;
};

var createReducer = function(entity, op) {
    var reduce = {};
    reduce[op] = entity;
    var reducer = {};
    reducer[(entity + "-" + op)] = reduce;
    return reducer;
};

var createDurationGroup = function(duration) {
    return {duration: duration};
}

var createSegmentGroup = function(segment) {
    return {segment: segment};
}