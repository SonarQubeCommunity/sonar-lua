function add_rr()--+1
a=3
end
function foo (a)--+2
       print("foo", a)--3
       return coroutine.yield(2*a)-- 4- return, 5-coroutine., 6-yield(2*a) 
     end
     
---     
 co = coroutine.create(function (a,b)--7-coroutine., 8-create(function (a,b)
           print("co-body", a, b)--9
           local r = foo(a+1)--10
           print("co-body", r)--11
           local r, s = coroutine.yield(a+b, a-b)--12,13
               print("co-body", r, s)--14
            return b, "end"--15
     end)    