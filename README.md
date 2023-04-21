# TypeDB Onboarding Project

## Domain: Olympiad achievements of students

## Dataset

Real data from one of the schools in Moscow (anonymized), originally stored in Google Sheets.

## Questions to explore

1. What teacher has the most number of regional-level olympiads prize-winners in a 2022-2023 school year?
2. Show the list of students that have right to participate in the Regional olympiad in informatics 2023. 
3. How successful in olympiads are students of different graduate years?

## How to load the data

1. Run TypeDB Version 2.16.1 at localhost:1729
2. Run `bazel run //:data-loader`

## How to run the tests

1. Run TypeDB Version 2.16.1 at localhost:1729
2. Run `bazel test //:test`

## Answers discovered!

1. What teacher has the most number of regional-level olympiads prize-winners in a 2022-2023 school year?

**Query**: [query1.tql](studio-project/query1.tql)

**Answer**: Keith Stewart
```
"Miss Janice Clarke" isa name => 2
"Dr Kirsty Miller" isa name => 2
"Nathan Hamilton" isa name => 2
"Miss Lucy Cooke" isa name => 5
"Dr Bryan Elliott" isa name => 8
"Keith Stewart" isa name => 23
"Mr Leonard James" isa name => 5
```

2. Show the list of students that have right to participate in the Regional olympiad in informatics 2023.

**Query**: [query2.tql](studio-project/query2.tql)

**Answer**:
```
{ $st_n "Amanda Walker" isa name; }
{ $st_n "Annette Bradley" isa name; }
{ $st_n "Barry Norman" isa name; }
{ $st_n "Bethany Nicholls" isa name; }
{ $st_n "Brian Shah" isa name; }
{ $st_n "Bryan Smith" isa name; }
{ $st_n "Cameron King" isa name; }
{ $st_n "Dawn Scott-Bates" isa name; }
{ $st_n "Denis Waters" isa name; }
{ $st_n "Donald Reid" isa name; }
{ $st_n "Donna Wright" isa name; }
{ $st_n "Dr Abigail Watson" isa name; }
{ $st_n "Dr Gareth Fowler" isa name; }
{ $st_n "Dr Garry Bryan" isa name; }
{ $st_n "Dr Gerard Wood" isa name; }
{ $st_n "Dr Paul Simpson" isa name; }
{ $st_n "Dr Sophie Baxter" isa name; }
{ $st_n "Dr Teresa Lawrence" isa name; }
{ $st_n "Edward Cook" isa name; }
{ $st_n "Elliott Evans" isa name; }
{ $st_n "Fiona Dean" isa name; }
{ $st_n "Geoffrey Taylor" isa name; }
{ $st_n "Graeme Gill" isa name; }
{ $st_n "Hannah Preston" isa name; }
{ $st_n "Iain Gibson" isa name; }
{ $st_n "Jamie Reeves" isa name; }
{ $st_n "Jay Miles" isa name; }
{ $st_n "Jayne Wilson" isa name; }
{ $st_n "Jenna Byrne" isa name; }
{ $st_n "Jill Richardson-Griffiths" isa name; }
{ $st_n "Karen Gordon" isa name; }
{ $st_n "Liam Richards-Jones" isa name; }
{ $st_n "Lisa Martin" isa name; }
{ $st_n "Lynn Walker" isa name; }
{ $st_n "Martyn Waters-Hutchinson" isa name; }
{ $st_n "Melissa Watkins" isa name; }
{ $st_n "Molly Brown" isa name; }
{ $st_n "Mr Daniel Baker" isa name; }
{ $st_n "Mr Glen Wright" isa name; }
{ $st_n "Mrs Maria West" isa name; }
{ $st_n "Mrs Mary Hartley" isa name; }
{ $st_n "Mrs Megan Gordon" isa name; }
{ $st_n "Ms Carly Giles" isa name; }
{ $st_n "Ms Olivia Reynolds" isa name; }
{ $st_n "Richard Young" isa name; }
{ $st_n "Ross Spencer-Carpenter" isa name; }
{ $st_n "Russell Martin" isa name; }
{ $st_n "Sharon Green-Wallace" isa name; }
{ $st_n "Shaun Parker" isa name; }
{ $st_n "Shirley Taylor" isa name; }
{ $st_n "Sian Bennett-Wood" isa name; }
{ $st_n "Sian Lord" isa name; }
{ $st_n "Stacey Hughes" isa name; }
{ $st_n "Stephanie Lewis" isa name; }
{ $st_n "Steven Thomas" isa name; }
{ $st_n "Vanessa Harding" isa name; }
```

3. How successful in olympiads are students of different graduate years?

**Query**: [query3.tql](studio-project/query3.tql)

**Answer**:
```
2025 isa graduate-year => 90
2026 isa graduate-year => 68
2027 isa graduate-year => 54
2028 isa graduate-year => 21
2021 isa graduate-year => 16
2022 isa graduate-year => 59
2023 isa graduate-year => 72
2024 isa graduate-year => 107
```

## Appendix: DB types

1. Attributes:
   - name
   - email
   - school-year
   - graduate-year
   - title
   - level
   - rank
   - score
   - id
   - grade
3. Entities:
   - person
   - student
   - teacher
   - group
   - olympiad
   - result
4. Relations:
   - participation
   - membership
   - teaching
   - prize-winning
5. Rules:
   - define-prize-winners
