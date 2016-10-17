  co = coroutine.create(function (a,b)--++2
      
          a=1     
     end)
 --
function add_rr()--+1
a=3
end
--
function foo (a)--+1
       print("foo", a)--+1
       return coroutine.yield(2*a)--+++3
     end
     
   