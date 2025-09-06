-- This file is executed on startup if spring.sql.init.mode=always
-- Inserting some standard vaccines
INSERT INTO vaccine (name, description) VALUES ('BCG', 'Bacillus Calmette-Guérin vaccine for tuberculosis.') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO vaccine (name, description) VALUES ('Polio', 'Oral Polio Vaccine for poliomyelitis.') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO vaccine (name, description) VALUES ('Hepatitis B', 'Vaccine for Hepatitis B virus.') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO vaccine (name, description) VALUES ('MMR', 'Measles, Mumps, and Rubella vaccine.') ON DUPLICATE KEY UPDATE name=name;