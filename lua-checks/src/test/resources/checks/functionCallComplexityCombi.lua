s.create(function (a,b)--+1

           a.rint("co-body", a, b)--2

           local r = foo(a+1)--3

           print("co-body", r)--4

          local r, s = yield(a+b, a-b)--5

           print("co-body", r, s)--6

            if i > 20 then --7

            local x          

             x = 20

             print(x + 2)--8

             else

             print(x)--9       

             end

            return b, "end" --11  

     end)
--test 2 
 lnlog.traceinfo("GUUS: Update vtype",{id=ctx.Input.TableId,
 name=ctx.Input.Name,keys=ctx.Input.SQL_Keys,definition=ctx.Input.SQL_Definition})
--test3 
 lnlog.traceinfo(modName..": Verwerk afstemstap record", 
 { nr=i, id=curRow[fi.id], soort=curRow(sohei[fi.refAfstemStapSoort]),
  resultaat=curRow[fi.Resultaat], omschrijving=(curRow[fi.Omschrijving] or "null"), 
  objectid =(curRow[fi.refObject]) })