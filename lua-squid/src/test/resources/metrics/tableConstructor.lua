local DoTheJob = function(ctx) 

  lnlog.traceinfo("addToSet", {set=std.tostring(set or 'nil'), key=std.tostring(key or 'nil')})
  set[key] = 'true'
         
end
 c={a,{},--kkkk
 b}

argcheck{}
--
--lnlog.+1
--traceinfo()+2
--{}+3
--std.+4
--tostring(set or 'nil')+5
--std.+6
--tostring(key or 'nil')+7
-- set[key]+8 [key] without "set" is geen complexity
 
 
 
 