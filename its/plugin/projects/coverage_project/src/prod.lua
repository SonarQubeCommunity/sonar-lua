 local M = {}

function M.func1()
  return 1
end

function M.func2()
  error( "test!" )
end



 
local function testFunction()
      print("Test function called")
end
M.testFunction = testFunction
 
return M