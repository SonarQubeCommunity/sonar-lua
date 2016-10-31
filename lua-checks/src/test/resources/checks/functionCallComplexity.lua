create(function (a,b)--+1

           print("co-body", a, b)--2

           local r = foo(a+1)--3

           print("co-body", r)--4

           local r, s = coroutine.yield(a+b, a-b)--5

           print("co-body", r, s)--6

            if i > 20 then --7

            local x          

             x = 20

             print(x + 2)--8

             else

             print(x)--9       

             end

            return b, "end" --10  

     end)
 traceinfo()--++2
 

