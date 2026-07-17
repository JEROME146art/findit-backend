package com.findit;

import com.findit.model.entity.Student;
import com.findit.repository.UserRepository;
import com.findit.voting.entity.*;
import com.findit.voting.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ElectionRepository electionRepository;
    private final BallotRecordRepository ballotRecordRepository;
    private final TownhallQuestionRepository townhallQuestionRepository;

    public DataInitializer(UserRepository userRepo, PasswordEncoder passwordEncoder,
                           ElectionRepository electionRepository,
                           BallotRecordRepository ballotRecordRepository,
                           TownhallQuestionRepository townhallQuestionRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.electionRepository = electionRepository;
        this.ballotRecordRepository = ballotRecordRepository;
        this.townhallQuestionRepository = townhallQuestionRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            Student student = new Student(
                    "john@college.edu",
                    passwordEncoder.encode("pass123"),  // 🔒 Encrypted!
                    "John Doe",
                    "9999999999",
                    "CS2024001",
                    "Computer Science"
            );
            userRepo.save(student);
            System.out.println("✅ Test student created — email: john@college.edu, password: pass123");
        }

        if (electionRepository.count() == 0) {
            // Seed SGA Election
            Election sga = new Election("SGA-2026", "2026-2027 Student Government Association (SGA) Executive Election", "SGA", "RCV", "All Enrolled Students", null);
            sga.setTotalVotes(3420);
            sga.addCandidate(new Candidate("Alex Rivera & Maya Lin", "Action Over Promises — 24/7 Library, Lower Dining Fees, Mental Health First", "Junior leaders committed to campus reforms.", "24/7 Library access during finals week.", "100% solar array installation on West Garage roof.", "Free weekend grocery shuttles.", 1368));
            sga.addCandidate(new Candidate("Jordan Vance & Chloe Bennett", "Innovate Our Campus — Green Energy, AI Study Hubs, Fair Student Parking", "Engineering and Econ majors building student tech hubs.", "Free enterprise ChatGPT / Claude access for undergraduates.", "Installing 40 high-speed EV chargers on campus.", "Revamping campus dining hours until 11 PM.", 1163));
            sga.addCandidate(new Candidate("Marcus Thorne & Liam O'Connor", "Student Voice & Transparency — Budget Accountability, Club Expansion", "Business administration students demanding budget transparency.", "Standardizing pass/fail conversion windows to 8 weeks.", "Campus community gardens for food pantry.", "24/7 campus safety shuttle routes.", 615));
            sga.addCandidate(new Candidate("Elena Rostova & David Kim", "Equitable Campus for All — Diversity & Inclusion, Emergency Grant Fund", "Advocating for student accessibility and equity.", "Free digital textbook portal for core classes.", "Free unlimited city bus and subway passes.", "Subsidized emergency on-campus housing.", 274));
            electionRepository.save(sga);

            // Seed Engineering Election
            Election eng = new Election("ENG-2026", "School of Engineering & Computer Science Senate Representative", "SENATE", "PLURALITY", "Engineering & Computer Science Majors Only", null);
            eng.setTotalVotes(1184);
            eng.addCandidate(new Candidate("Priya Sharma", "Open Source Campus, High-Performance GPU Lab Access, 24/7 Hacker Space", "ACM Student Chapter President.", "Dedicated NVIDIA H100 cluster allocations.", "Energy-efficient server scheduling.", "More vending options in Engineering building.", 542));
            eng.addCandidate(new Candidate("Devon Carter", "More Industry Co-ops, Annual Hackathon Funding, Modernizing CS Curriculum", "Mechanical Engineering Senior with top industry internships.", "Expanding industry co-op networking fairs.", "Recycling electronic waste via department bins.", "Better lockers and storage for heavy equipment.", 412));
            eng.addCandidate(new Candidate("Zackary Nguyen", "Robotics Club Expansion, Better Lab Equipment, Study Group Network", "Robotics Team Lead and EE Sophomore.", "Replacing outdated oscilloscopes and soldering stations.", "Solar-powered charging benches around quad.", "Adding soundproof quiet booths in library.", 230));
            electionRepository.save(eng);

            // Seed Referendum
            Election ref = new Election("REF-104", "Campus-Wide Referendum: Proposition 104 — Late-Night Campus Safety & Shuttle Expansion", "REFERENDUM", "REFERENDUM", "All Enrolled Students", "Shall the Student Union allocate $45,000 from the annual surplus reserve to expand the campus night shuttle routes (running every 15 minutes from 10 PM to 4 AM) and install emergency blue-light safety stations near the West Campus Dormitories and Engineering Quad?");
            ref.setTotalVotes(4120);
            ref.addCandidate(new Candidate("👍 YES - Approve Safety & Shuttle Expansion", "Allocates $45k surplus to 15-minute night shuttles.", "Proposition Yes option", null, null, null, 2810));
            ref.addCandidate(new Candidate("👎 NO - Reject Proposition (Keep Funds in Reserve)", "Maintains current shuttle schedule.", "Proposition No option", null, null, null, 1100));
            ref.addCandidate(new Candidate("✋ ABSTAIN - Record Attendance Without Stance", "Counts toward quorum without voting Yes or No.", "Proposition Abstain option", null, null, null, 210));
            electionRepository.save(ref);

            // Seed Audit Ledger
            ballotRecordRepository.save(new BallotRecord("#UV-2026-SGA-8F92A1", "SGA-2026", "SGA Executive", "1st: Alex Rivera & Maya Lin, 2nd: Jordan Vance..."));
            ballotRecordRepository.save(new BallotRecord("#UV-2026-ENG-3D71C2", "ENG-2026", "Engineering Senate", "Choice: Priya Sharma (ACM Pres)"));
            ballotRecordRepository.save(new BallotRecord("#UV-2026-REF-8F21A9", "REF-104", "Referendum 104", "Choice: 👍 YES - Approve Safety & Shuttle Expansion"));

            // Seed Townhall Questions
            townhallQuestionRepository.save(new TownhallQuestion("Alex Rivera & Maya Lin", "Dormitory & Dining", "The lines at West Campus Dining Hall take over 40 minutes between 12 PM and 1 PM every day. How exactly do you plan to fix this dining bottleneck?", "Jordan Smith (Verified Senior)", 42, "We have already met with Campus Dining Services. Our proposal adds two new mobile pickup kiosks for grab-and-go sandwich/bowl orders during peak hours!"));
            townhallQuestionRepository.save(new TownhallQuestion("All SGA Candidates", "Academic & Finals Schedule", "Will you guarantee that the Main Library and Engineering Study Halls stay open 24 hours a day during all 10 days of the Spring Finals period?", "Anonymous Verified Student", 38, "Yes! All four SGA tickets have endorsed our joint petition guaranteeing 24/7 library access with university security during midterms and finals."));

            System.out.println("✅ UniVote AI College Voting database initialized with live elections, candidates, audit ledger, and townhall Q&As!");
        }
    }
}