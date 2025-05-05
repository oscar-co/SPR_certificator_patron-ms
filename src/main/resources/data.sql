DELETE FROM conversion_factor;

-- Presiones
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'Pa', 'bar', 1e-5);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'Pa', 'mbar', 0.01);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'Pa', 'mmHg', 0.00750062);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'Pa', 'mmH2O', 0.1019716);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'Pa', 'psi', 0.000145038);

INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'bar', 'Pa', 100000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'bar', 'mbar', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'bar', 'mmHg', 750.063);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'bar', 'mmH2O', 10197.162);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'bar', 'psi', 14.5038);

INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mbar', 'Pa', 100.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mbar', 'bar', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mbar', 'mmHg', 0.750063);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mbar', 'mmH2O', 10.1972);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mbar', 'psi', 0.0145038);

INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmHg', 'Pa', 133.322);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmHg', 'bar', 0.00133322);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmHg', 'mbar', 1.33322);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmHg', 'mmH2O', 13.5951);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmHg', 'psi', 0.0193367);

INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmH2O', 'Pa', 9.80665);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmH2O', 'bar', 9.80665e-5);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmH2O', 'mbar', 0.0980665);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmH2O', 'mmHg', 0.0735561);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'mmH2O', 'psi', 0.00142233);

INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'psi', 'Pa', 6894.76);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'psi', 'bar', 0.0689476);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'psi', 'mbar', 68.9476);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'psi', 'mmHg', 51.7151);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('presion', 'psi', 'mmH2O', 703.07);


-- temperatura
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'C', 'K', 1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'K', 'C', 1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'C', 'F', 1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'F', 'C', 1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'K', 'F', 1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('temperatura', 'F', 'K', 1);


-- Masa
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'kg', 'g', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'kg', 'mg', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'kg', 'µg', 1000000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'kg', 'ton', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'g', 'kg', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'g', 'mg', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'g', 'µg', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'g', 'ton', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'mg', 'kg', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'mg', 'g', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'mg', 'µg', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'mg', 'ton', 1e-09);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'µg', 'kg', 1e-09);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'µg', 'g', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'µg', 'mg', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'µg', 'ton', 1e-12);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'ton', 'kg', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'ton', 'g', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'ton', 'mg', 1000000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('masa', 'ton', 'µg', 1000000000000.0);


-- Longitud
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'm', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'cm', 100000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'mm', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'µm', 1000000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'ft', 3280.839895);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'km', 'mi', 0.621371);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'km', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'cm', 100.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'mm', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'µm', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'ft', 3.280839895);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'm', 'mi', 0.000621371);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'km', 1e-05);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'm', 0.01);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'mm', 10.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'µm', 10000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'ft', 0.0328084);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'cm', 'mi', 6.2137e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'km', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'm', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'cm', 0.1);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'µm', 1000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'ft', 0.00328084);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mm', 'mi', 6.2137e-07);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'km', 1e-09);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'm', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'cm', 0.0001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'mm', 0.001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'ft', 3.2808e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'µm', 'mi', 6.2137e-10);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'km', 0.0003048);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'm', 0.3048);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'cm', 30.48);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'mm', 304.8);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'µm', 304800.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'ft', 'mi', 0.000189394);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'km', 1.60934);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'm', 1609.34);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'cm', 160934.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'mm', 1609340.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'µm', 1609340000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('longitud', 'mi', 'ft', 5280.0);


-- Area
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'm²', 'km²', 1e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'm²', 'ha', 0.0001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'm²', 'cm²', 10000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'm²', 'ft²', 10.763915051182416);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'm²', 'mi²', 3.86102158592535e-07);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'km²', 'm²', 1000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'km²', 'ha', 100.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'km²', 'cm²', 10000000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'km²', 'ft²', 10763915.051182415);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'km²', 'mi²', 0.38610215859253505);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ha', 'm²', 10000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ha', 'km²', 0.01);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ha', 'cm²', 100000000.0);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ha', 'ft²', 107639.15051182416);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ha', 'mi²', 0.0038610215859253504);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'cm²', 'm²', 0.0001);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'cm²', 'km²', 1e-10);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'cm²', 'ha', 1e-08);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'cm²', 'ft²', 0.0010763915051182416);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'cm²', 'mi²', 3.8610215859253505e-11);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ft²', 'm²', 0.092903);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ft²', 'km²', 9.2903e-08);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ft²', 'ha', 9.2903e-06);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ft²', 'cm²', 929.03);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'ft²', 'mi²', 3.5870048839722284e-08);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'mi²', 'm²', 2589988.11);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'mi²', 'km²', 2.5899881099999997);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'mi²', 'ha', 258.998811);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'mi²', 'cm²', 25899881099.999996);
INSERT INTO conversion_factor (magnitud, u_entrada, u_salida, factor) VALUES ('area', 'mi²', 'ft²', 27878411.9996125);