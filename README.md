# TypeDB Onboarding Project

1. Domain: Olympiad achievements of students
2. Attributes:
   - name
   - email
   - year
   - title
   - level
   - rank
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
   - participant with a result != 'participant' is a prize-winner
6. Questions:
   - What teacher has the most number of regional-level olympiads prize-winners in a given year?
   - Show the list of students that have right to participate in the ROI in a given year.
   - How successful in olympiads are students of different graduate years?
