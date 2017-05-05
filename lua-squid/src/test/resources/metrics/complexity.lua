function add_rr()--+1
a=3
end
function foo (a)--+2
       print("foo", a)
       return coroutine.yield(2*a)-- 3, 
     end
     
---     
 
