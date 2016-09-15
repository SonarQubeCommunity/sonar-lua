local function add (a,b)--2
       print("foo", a)
       return coroutine.yield(2*a)
     end

function add (a,b)--2
       print("foo", a)
       return coroutine.yield(2*a)
     end

x = 10
    local i = 1        -- local to the chunk
    
    while i<=x do
      local x = i*2    -- local to the while body
      print(x)         --> 2, 4, 6, 8, ...
      i = i + 1
    end
 --if statements   
    if i > 20 then
    function add (a,b)--2
       print("foo", a)
       return coroutine.yield(2*a)
     end
      local x          -- local to the "then" body
      x = 20
      print(x + 2)
    else
      print(x)         --> 10  (the global one)
    end
    
   --
   if op == "+" then
      r = a + b
    elseif op == "-" then
      r = a - b
    elseif op == "*" then
      r = a*b
    elseif op == "/" then
      r = a/b
    else
      error("invalid operation")
    end
   -- 
   
  local found = nil
    for i=1,a.n do
      if a[i] == value then
        found = i      -- save value of `i'
        break
      end
    end
    --for
 
    
    for i,v in ipairs(a)  do
        sum = sum + 1
      end
      
      
       repeat
      line = os.read()
    until line ~= ""
    print(line)
     --
