CREATE DATABASE `healthmessagesexchange` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `messagequeue` (
  `control_id` int(11) DEFAULT NULL,
  `xmlmessage` varchar(20000) DEFAULT NULL,
  `last_accessed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `healthmessagesexchange`.`messagequeue`
(`control_id`,
`xmlmessage`,
`last_accessed`)
VALUES
(
'12', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Msg id = c123>\n<patient>\n<patientId>12345</patientId>\n<patientRole/>\n<GivenName>Adam</GivenName>\n<FamilyName>Smith</FamilyName>\n<BirthTime>19541125</BirthTime>\n<providerId>2.16.840</providerId>\n<creationDate></creationDate>\n</patient>\n<Guardian>\n<GuardianNo >45678</GuardianNo>\n<Relationship>Grandfather</Relationship>\n<GivenName>Ralph</GivenName>\n<FamilyName>Finnes</FamilyName>\n<phone>781-555-1212</phone>\n<address>12, Finnegan st., </address>\n<city>Boston</city><state>MA</state>\n<zip>02318</zip></Guardian><Author>\n<AuthorId>KP00017</AuthorId><AuthorTitle>Mr.</AuthorTitle>\n<AuthorFirstName>Henry</AuthorFirstName><AuthorLastName>Seven</AuthorLastName><ParticipatingRole>InsuranceCompany</ParticipatingRole></Author><InsuranceCompany><PayerId>3456</PayerId><Name>Good Health Insurance</Name><PolicyHolder>Patient’s father</PolicyHolder><PolicyType>Extended healthcare </PolicyType><Purpose>Kidney failure</Purpose></InsuranceCompany><FamilyHistory><RelativeId>45565</RelativeId><Relationship>Grandmother</Relationship><age>67</age><Diagnosis>Diabetes</Diagnosis></FamilyHistory><Allergies><Id>4556</Id><Substance>Codeine</Codeine><Reaction>Hives</Reaction><Status>Active</Status></Allergies><LabTestReports><LabTestResultId> 200203</LabTestResultId><PatientVisitId> 2043</PatientVisitId><LabTestPerformedDate> 20141212</LabtestPerformedDate><LabTestType> HGB</Labtesttype><TestResultValue> </testResultValue><ReferenceRangeHigh>18 g/dl </ReferenceRangeHigh><ReferenceRangeLow>13 g/dl </ReferenceRangeLow></LabTestReports><Plan><Plan Id>456</PlanId><Activity>Colonoscopy</Activity><Date>April 21, 2015</Date></Plan></Msg>', '2015-02-19 15:12:14'
);

