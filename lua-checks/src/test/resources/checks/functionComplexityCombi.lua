------------------------------------------------------------------------------------------------------------------------
--            Leest de behoeftes en omreidroutes die op een template getoond moeten aan de hand van de ingevulde
--            selectie criteria.
-- Author   : Guus Ouwerkerk (InTraffic)
-- Date     : 2016-01-17
-- Invoer   : De collectie "OverzichtFilter" in de sessie context
--            Wanneer in de beschrijvingen van de velden in deze collectie de term object(en) wordt gebruikt verwijst
--            dit naar behoefte(s) en/of omreidroute(s).
--  * DagFilter         : Geeft aan of het standaard filter of een door de gebruiker gedefinieerd filter wordt gebruikt
--  * Dagen{}           : (Alleen bij standaard filter) Een array met de geselecteerde dagnummers.
--  * AanvangDag        : (Alleen bij niet standaard filter) Dagnummer waarna een object actueel moet zijn.
--  * AanvangTijd       : (Alleen bij niet standaard filter) Tijdstip in de AanvangDag (in minuten) waarna een object
--                        actueel moet zijn.
--  * EindeDag          : (Alleen bij niet standaard filter) Dagnummer waarvoor een object actueel moet zijn.
--  * EindeTijd         : (Alleen bij niet standaard filter) Tijdstip in de EindeDag (in minuten) waarvoor een object
--                        actueel moet zijn.
--  * WeekNummers{}     : Array met weeknummers waarbinnen objecten actueel moeten zijn binnen de opgegeven tijdliggingen.
--  * DatumVan          : Begindatum van de periode dat objecten actueel moeten zijn.
--  * DatumTot          : Eindddatum van de periode dat objecten actueel moeten zijn.
--  * Duur              : Selectiecriterium voor de duur van IO's bestaande uit de vergelijkings bewerking en de
--                        grenswaarde (bijv. ">=4").
--  * Entiteiten        : Array met id's van te selecteren behoeftetypes (-1 voor omreidroutes).

-- LET OP:  Vanuit het scherm wordt een dagnummer als 1(=maandag) t/m 7(=zondag) doorgegeven.
--          In de SQL-queries worden deze dagnummers gebruikt in combinatie met de tabel weeknummers. Om de juiste datum
--          van een dag in de week te berekenen moet hiervoor het dagnummer zero based worden.
------------------------------------------------------------------------------------------------------------------------
-- History:
-- Date:      | Name                  | Description
--------------+-----------------------+---------------------------------------------------------------------------------
-- 2016-03-22 | Matthijs Datema       | Routes naar Opstellocatie toegevoegd aan trajecten.
--------------+-----------------------+---------------------------------------------------------------------------------

local ms_ModName = "OverzichtFilter"
-- --if ( ctx.__Log ) then
  -- --tinsert(ctx.__Log, "-------------------------------------------------------------------------------")
-- --else
  -- ctx.__Log = {}
-- --end
-- function AddLog(msg, info)
  -- if ( type(info) == "table" ) then
    -- local i=1
    -- for k,v in info do
      -- tinsert(%ctx.__Log, msg .. "(x" .. i .. "): [" .. k .. "] = [" .. (std.tostring(v) or "nil") .. "]")
      -- i = i + 1
    -- end
  -- else
    -- tinsert(%ctx.__Log, msg .. ((info and (": [" .. std.tostring(info) .. "]")) or ""))
  -- end
-- end

-- Functie voor het toevoegen van een element aan teken/string gescheiden string
local StrConcat = function(as_String, as_Seperator, as_Value)
  if ( ( as_String or "" ) == "" ) then
    return as_Value;
  end
  return as_String .. as_Seperator .. as_Value
end
-- Veldnamen per gelezen query. Wordt bij de betreffende query gevuld
local mc_Fields = {}

local Laad_Behoeftes = function(ao_Ctx, ac_Fields)

  local lo_Db = ao_Ctx.Db--1
  local lo_Filter = ao_Ctx.OverzichtFilter--2

  -- Tijdelijke tabellen om de geselecteerde periode en tijdligging(en) op te slaan
  lo_Db.Command = [[  
    SET NOCOUNT ON;]]--3 
    -- CREATE TABLE cmpOverzichtTijdligging
      -- ( VanDag   INT
      -- , VanTijd    INT
      -- , TotDag     INT
      -- , TotTijd    INT
      -- );]]
-- Bij een standaarddagfilter altijd de tijdligging vandag-7:00 totdag-6:59 (totdag is 1 dag later als de van dag.
  -- De geselecteerde dagen in array dagen van het filter object zijn 1=maandag t/m 7= zondag.
  -- Voor de berekening in de SQL query wordt dit dagnummer opgeteld bij de 1e dag van de week in tabel weeknummers.
  -- Daar dit ook altijd een maandag is moet het dagnummer voor de berekening dus zero based zijn,
  lo_Db.Command = lo_Db.Command .. [[
      DELETE FROM cmpOverzichtTijdligging WHERE Spid = @@SPID;
      DELETE FROM cmpOverzichtSelectie WHERE Spid = @@SPID;
      DELETE FROM cmpOverzichtPeriode WHERE Spid = @@SPID; ]]--4,5 vanwege .
 for li_BehIdx=1, getn(aa_Behoeftes) do
    local la_CurBehoefte = aa_Behoeftes[li_BehIdx]
      -- Vergelijk met de volgende rij
      -- Is het een nieuwe behoefte
    if ( la_CurBehoefte[1] ~= li_BehoefteId ) or ( la_CurBehoefte[7] ~= li_Bds ) then
      -- Maak de nieuwe behoefte de huidige
      li_BehoefteId = la_CurBehoefte[lc_FldBehoefte.BehId]
      li_Bds = la_CurBehoefte[lc_FldBehoefte.Bds]
      -- Initialiseer een grafisch object voor de behoefte
      
     

      -- Plaats het grafische object van de behoefte in het grafische array van de sessie data
      tinsert(ao_Ctx.grafisch.behoeftes, lc_Grafisch )

      -- Voeg alle geselecteeerde tekening elementen toe.
      -- Het array met geselecteerde tekening elementen is gesorteerd op behoefte-id (net als de behoefte array) zodat we de geselecteerde tekening elementen
      -- toe moeten voegen totdat we een andere behoefte-id in de geselecteerde tekening elementen vinden (of wanneer we een het einde van deze array zijn)
      while ( li_SelIdx <= li_SelMax ) and ( la_Selecties[li_SelIdx][1] == li_BehoefteId ) and ( la_Selecties[li_SelIdx][3] == li_Bds ) do       
        local la_CurTekening = la_Selecties[li_SelIdx]
        -- Voeg het tekening dienstregelpunten toe         
        tinsert(lc_Grafisch.DienstRegelPunten, la_Selecties[li_SelIdx][lc_FldElementen.Drp])
        -- Positioneer op het volgende tekening dienstregelpunt
        li_SelIdx = li_SelIdx + 1
      end
    end
  
 end end