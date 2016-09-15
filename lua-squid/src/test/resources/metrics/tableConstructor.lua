polyline = {color="blue", thickness=2, npoints=4,
                 {x=0,   y=0},
                 {x=-10, y=0},
                 {x=-10, y=1},
                 {x=0,   y=1}
               }
       list = nil
    for line in io.lines() do
    list = {next=list, value=line}
    end     
     lc_Grafisch = { ID                  = la_CurBehoefte[lc_FldBehoefte.BehId]
                    , BehoefteType        = la_CurBehoefte[lc_FldBehoefte.BehType]
                    , Identificatie       = la_CurBehoefte[lc_FldBehoefte.BehIdent]
                    , Geselecteerd        = ( la_CurBehoefte[lc_FldBehoefte.BehIdent] == ( lo_Filter.Identificatie or "" ) )
                    , EvenementNaam       = la_CurBehoefte[lc_FldBehoefte.EvtNaam]
                    , EvenementTypeKader  = la_CurBehoefte[lc_FldBehoefte.EvtType]
                    , Status              = la_CurBehoefte[lc_FldBehoefte.ObjStatus]
                    , Duur                = la_CurBehoefte[lc_FldBehoefte.Duur]
                    , DagTijdAanvang      = la_CurBehoefte[lc_FldBehoefte.Van]
                    , DagTijdEinde        = la_CurBehoefte[lc_FldBehoefte.Tot]
                    , Buitendienststelling = la_CurBehoefte[lc_FldBehoefte.Bds]
                    , ToolTip             = "<B>" .. la_CurBehoefte[lc_FldBehoefte.BehIdent] .. "</B>"
                    , secties             = {}
                    , spoortakken         = {}
                    , lijnstukken         = {}
                    }