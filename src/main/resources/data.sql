DELETE FROM conversion_factor;

-- Pa a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'kPa', 0.001);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'MPa', 0.000001);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'bar', 0.00001);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'mbar', 0.01);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'atm', 9.8692e-6);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'mmHg', 0.00750062);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'cmHg', 0.000750062);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'psi', 0.000145038);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('Pa', 'kg/cm²', 1.0197e-5);

-- bar a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'Pa', 100000);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'kPa', 100);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'MPa', 0.1);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'mbar', 1000);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'atm', 0.986923);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'mmHg', 750.062);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'cmHg', 75.0062);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'psi', 14.5038);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('bar', 'kg/cm²', 1.0197);

-- atm a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'Pa', 101325);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'kPa', 101.325);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'MPa', 0.101325);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'bar', 1.01325);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'mbar', 1013.25);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'mmHg', 760);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'psi', 14.6959);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('atm', 'kg/cm²', 1.03323);

-- psi a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('psi', 'Pa', 6894.76);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('psi', 'kPa', 6.89476);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('psi', 'bar', 0.0689476);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('psi', 'atm', 0.068046);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('psi', 'kg/cm²', 0.070307);

-- mbar a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mbar', 'Pa', 100);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mbar', 'bar', 0.001);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mbar', 'kPa', 0.1);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mbar', 'atm', 9.8692e-4);

-- mmHg a otras unidades
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mmHg', 'Pa', 133.322);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mmHg', 'bar', 0.00133322);
INSERT INTO conversion_factor (u_entrada, u_salida, factor) VALUES ('mmHg', 'atm', 0.00131579);

-- Puedes seguir agregando combinaciones si quieres cerrar el 10x10 completo
