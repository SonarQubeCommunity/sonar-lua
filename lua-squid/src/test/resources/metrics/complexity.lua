function add_rr()--+1
a=3
end
function foo (a)--+2
       print("foo", a)
       return coroutine.yield(2*a)-- 3, 
     end
     
---     
 co = coroutine.create(function (a,b)--4
           print("co-body", a, b)
           local r = foo(a+1)
           print("co-body", r)
           local r, s = coroutine.yield(a+b, a-b)
               print("co-body", r, s)
            return b, "end"
     end)    
