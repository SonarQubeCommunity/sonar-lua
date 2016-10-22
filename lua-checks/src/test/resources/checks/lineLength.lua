     lo_Db.Command = lo_Db.Command .. [[
    DECLARE @Van DATETIME;
    DECLARE @Tot DATETIME;
    SET @Van = dbo.fnSqlDateTime(]] .. lo_Filter.DatumVan .. [[);
       SET @Tot = DATEADD(MINUTE, 1439, dbo.fnSqlDateTime(]] .. lo_Filter.  DatumTot .. [[) );
    INSERT INTO     cmpOverzichtPeriode
                    ( Spid
                    , Van
                    , Tot
                    )
      SELECT DISTINCT @@Spid
                 
        FROM        tblWeekNummer W
        INNER JOIN  cmpOverzichtTijdligging D
                ON    ( D.Spid = @@SPID )
        WHERE       (  ( W.SqlDatumVan BETWEEN @Van AND @Tot )
                    OR ( W.SqlDatumTot BETWEEN @Van AND @Tot ) 
          OR ( W.SqlDatumVan < @Van AND W.SqlDatumTot > @Tot ) )
          ]] .. ls_Weken .. [[;
        DELETE FROM   cmpOverzichtPeriode
          WHERE       ( Tot < @Van )
             OR       ( Van > @Tot );
        UPDATE        cmpOverzichtPeriode
          SET         Van = @Van
          WHERE       ( Van < @Van );
        UPDATE        cmpOverzichtPeriode
          SET         Tot = @Tot
          WHERE       ( Tot > @Tot );
                  , DATEADD(MINUTE,D.VanTijd,DATEADD(DAY,D.VanDag,CAST(W.SqlDatumVan AS DATETIME)))
                  , DATEADD(MINUTE,D.TotTijd,DATEADD(DAY,D.TotDag,CAST(W.SqlDatumVan AS DATETIME)))
          ]]
          
          
          