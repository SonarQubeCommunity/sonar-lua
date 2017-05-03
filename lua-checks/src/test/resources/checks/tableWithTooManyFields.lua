lnlog.traceinfo(modName..": Verwerk afstemstap record", { nr=i, id=curRow[fi.id], 
soort=curRow[fi.refAfstemStapSoort], resultaat=curRow[fi.Resultaat], omschrijving=(curRow[fi.Omschrijving] or "null"),
 objectid =(curRow[fi.refObject]) })   


lnlog.traceinfo("addToSet", {set=std.tostring(set or 'nil'),id=curRow[fi.id],set=std.tostring(set or 'nil'),
omschrijving=(curRow[fi.Omschrijving] or "null"), key=std.tostring(key or 'nil')})

lnlog.traceinfo("addToSet", {set=std.tostring(set or 'nil'), key=std.tostring(key or 'nil')})

