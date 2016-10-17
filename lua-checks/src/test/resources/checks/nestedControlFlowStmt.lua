if op == "+" then
      r = a + b
    if op == "-" then
      r = a - b
    if op == "*" then
      r = a*b
    if op == "/" then--break
      r = a/b
      end
    else
      error("invalid operation")
    end
end
end
polyline = {color="blue", thickness=2, npoints=4,
                 {x=0,{a,{c}},y=0},
                 {x=-10, y=0},
                 {x=-10, y=1},
                 {x=0,   y=1}
               }