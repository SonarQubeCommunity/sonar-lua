function add (a,b)
       print("foo", a)
       return coroutine.yield(2*a)--4
     end