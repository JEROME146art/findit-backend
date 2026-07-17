/**
 * UniVote AI — Shared Data & LocalStorage State Manager
 * Handles persistent state across all multi-page files.
 */

const INITIAL_ELECTIONS = [
    {
        id: 'SGA-2026',
        title: '2026-2027 Student Government Association (SGA) Executive Election',
        category: 'SGA',
        votingStrategy: 'RCV', // RCV, PLURALITY, REFERENDUM
        eligibility: 'All Enrolled Students (Freshman - Graduate)',
        status: 'Live',
        closesIn: '18 hours, 24 mins',
        totalVotes: 3420,
        hasVoted: false,
        candidates: [
            {
                id: 'cand-sga-1',
                name: 'Alex Rivera & Maya Lin',
                slogan: 'Action Over Promises — 24/7 Library, Lower Dining Fees, Mental Health First',
                bio: 'Alex is a Junior in Political Science and Maya is a Junior in Data Science. We have already negotiated with the Provost to secure $100k for overnight library staff during midterms and finals.',
                academic: '24/7 Library access during finals week, syllabus transparency guaranteed before drop deadlines, and $500 max cap on required physical textbook costs.',
                sustainability: '100% solar array installation on West Garage roof by Spring 2027, free reusable dining hall containers, and expanding EV shuttle buses.',
                housing: 'Free weekend grocery shuttles for off-campus students, $20,000 emergency student micro-grant fund, and 20% boost to club event budgets.',
                votes: 1368
            },
            {
                id: 'cand-sga-2',
                name: 'Jordan Vance & Chloe Bennett',
                slogan: 'Innovate Our Campus — Green Energy, AI Study Hubs, Fair Student Parking',
                bio: 'Jordan is a Senior in Electrical Engineering and Chloe is a Junior in Economics. Our focus is bringing cutting-edge AI tutors, guaranteed sophomore campus parking passes, and solar dorms.',
                academic: 'Free enterprise ChatGPT / Claude access for every undergrad, creating 12 new group collaborative tech rooms in the student union.',
                sustainability: 'Eliminating all single-use plastics from campus cafes within 6 months, installing 40 new high-speed EV chargers in campus lots.',
                housing: 'Revamping campus dining hall dinner hours until 11 PM and ending mandatory commuter meal plan requirements.',
                votes: 1163
            },
            {
                id: 'cand-sga-3',
                name: 'Marcus Thorne & Liam O\'Connor',
                slogan: 'Student Voice & Transparency — Budget Accountability, Club Expansion, Campus Shuttle',
                bio: 'Marcus and Liam are both Sophomores in Business Administration. We want to publish every single dollar spent by the administration and double annual funding for student-run clubs.',
                academic: 'Standardizing pass/fail conversion windows to 8 weeks and expanding undergraduate research grant stipends to $3,000.',
                sustainability: 'Campus community gardens where students can grow organic produce for the food pantry.',
                housing: '24/7 campus safety shuttle routes connecting off-campus student apartments directly to the engineering and arts quads.',
                votes: 615
            },
            {
                id: 'cand-sga-4',
                name: 'Elena Rostova & David Kim',
                slogan: 'Equitable Campus for All — Diversity & Inclusion, Emergency Grant Fund, Free Transit Pass',
                bio: 'Elena is a Senior in Sociology and David is a Junior in Public Health. Our campaign ensures every student regardless of financial background has equal access to wellness, transit, and textbooks.',
                academic: 'Free digital textbook portal for all 100-level and 200-level core university classes.',
                sustainability: 'Partnering with city transit to provide 100% free unlimited city bus and subway passes with student ID.',
                housing: 'Subsidized on-campus emergency housing for students facing unexpected financial or family distress.',
                votes: 274
            }
        ]
    },
    {
        id: 'ENG-2026',
        title: 'School of Engineering & Computer Science Senate Representative',
        category: 'SENATE',
        votingStrategy: 'PLURALITY',
        eligibility: 'Engineering & Computer Science Majors Only',
        status: 'Live',
        closesIn: '18 hours, 24 mins',
        totalVotes: 1184,
        hasVoted: false,
        candidates: [
            {
                id: 'cand-eng-1',
                name: 'Priya Sharma',
                slogan: 'Open Source Campus, High-Performance GPU Lab Access, 24/7 Hacker Space',
                bio: 'Computer Science Junior & President of the ACM Student Chapter.',
                academic: 'Dedicated NVIDIA H100 cluster allocations for undergraduate capstone and AI projects without requiring grad advisor sign-off.',
                sustainability: 'Energy-efficient server scheduling for computer labs.',
                housing: 'More vending options and coffee stations in the Engineering building.',
                votes: 542
            },
            {
                id: 'cand-eng-2',
                name: 'Devon Carter',
                slogan: 'More Industry Co-ops, Annual Hackathon Funding, Modernizing CS Curriculum',
                bio: 'Mechanical Engineering Senior with internships at Tesla and Lockheed.',
                academic: 'Expanding industry co-op networking fairs and updating senior design budgets.',
                sustainability: 'Recycling electronic waste directly through department collection bins.',
                housing: 'Better lockers and storage for heavy engineering equipment.',
                votes: 412
            },
            {
                id: 'cand-eng-3',
                name: 'Zackary Nguyen',
                slogan: 'Robotics Club Expansion, Better Lab Equipment, Study Group Network',
                bio: 'Electrical Engineering Sophomore & Robotics Team Lead.',
                academic: 'Replacing outdated oscilloscopes and soldering stations in 3rd-floor labs.',
                sustainability: 'Solar-powered charging benches around the Engineering quad.',
                housing: 'Adding soundproof quiet booths in the engineering library.',
                votes: 230
            }
        ]
    },
    {
        id: 'REF-104',
        title: 'Campus-Wide Referendum: Proposition 104 — Late-Night Campus Safety & Shuttle Expansion',
        category: 'REFERENDUM',
        votingStrategy: 'REFERENDUM',
        eligibility: 'All Enrolled Students (Freshman - Graduate)',
        status: 'Live',
        closesIn: '18 hours, 24 mins',
        totalVotes: 4120,
        hasVoted: false,
        propositionText: 'Shall the Student Union allocate $45,000 from the annual surplus reserve to expand the campus night shuttle routes (running every 15 minutes from 10 PM to 4 AM) and install emergency blue-light safety stations near the West Campus Dormitories and Engineering Quad?',
        candidates: [
            { id: 'prop-yes', name: '👍 YES - Approve Safety & Shuttle Expansion', slogan: 'Allocates $45k surplus to 15-minute night shuttles and blue-light stations.' },
            { id: 'prop-no', name: '👎 NO - Reject Proposition (Keep Funds in Reserve)', slogan: 'Maintains current shuttle schedule and preserves emergency surplus fund.' },
            { id: 'prop-abstain', name: '✋ ABSTAIN - Record Attendance Without Stance', slogan: 'Counts toward overall campus voter quorum without voting Yes or No.' }
        ]
    }
];

const INITIAL_TOWNHALL = [
    {
        id: 'qa-1',
        target: 'Alex Rivera & Maya Lin',
        category: 'Dormitory & Dining',
        question: 'The lines at West Campus Dining Hall take over 40 minutes between 12 PM and 1 PM every day. How exactly do you plan to fix this dining bottleneck?',
        author: 'Jordan Smith (Verified Senior)',
        timestamp: '2 hours ago',
        upvotes: 42,
        hasUpvoted: false,
        answer: 'We have already met with Campus Dining Services. Our proposal adds two new mobile pickup kiosks for grab-and-go sandwich/bowl orders during peak hours, and expands the dining app preorder window by 30 minutes. This will cut line times in half!'
    },
    {
        id: 'qa-2',
        target: 'All SGA Candidates',
        category: 'Academic & Finals Schedule',
        question: 'Will you guarantee that the Main Library and Engineering Study Halls stay open 24 hours a day during all 10 days of the Spring Finals period?',
        author: 'Anonymous Verified Student',
        timestamp: '4 hours ago',
        upvotes: 38,
        hasUpvoted: false,
        answer: 'Yes! All four SGA tickets have endorsed our joint petition guaranteeing 24/7 library access with university security and free midnight coffee stations during midterms and finals.'
    },
    {
        id: 'qa-3',
        target: 'Jordan Vance & Chloe Bennett',
        category: 'Sustainability & Green Campus',
        question: 'Your platform mentions installing 40 new EV chargers. Where will the funding come from without raising student parking permit fees?',
        author: 'Liam K. (Junior, Economics)',
        timestamp: '6 hours ago',
        upvotes: 29,
        hasUpvoted: false,
        answer: 'Great question! The funding will be 80% covered by federal/state clean transit infrastructure grants that the university has already pre-qualified for, with the remaining 20% matched by campus sustainability grant endowments. Student parking permit fees will not increase by a single cent!'
    },
    {
        id: 'qa-4',
        target: 'Engineering Candidates',
        category: 'Club Budget & Tuition Transparency',
        question: 'Can we get swipe card 24/7 access to the soldering and 3D printing labs for senior design teams?',
        author: 'Sarah M. (Senior, EE)',
        timestamp: '8 hours ago',
        upvotes: 24,
        hasUpvoted: false,
        answer: null
    }
];

const INITIAL_LEDGER = [
    { token: '#UV-2026-SGA-8F92A1', category: 'SGA Executive', summary: '1st: Alex Rivera & Maya Lin, 2nd: Jordan Vance...', timestamp: '2026-07-17 16:32:14 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-ENG-3D71C2', category: 'Engineering Senate', summary: 'Choice: Priya Sharma (ACM Pres)', timestamp: '2026-07-17 16:29:45 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-REF-8F21A9', category: 'Referendum 104', summary: 'Choice: 👍 YES - Approve Safety & Shuttle Expansion', timestamp: '2026-07-17 16:25:10 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-SGA-4A19B8', category: 'SGA Executive', summary: '1st: Jordan Vance & Chloe Bennett, 2nd: Alex Rivera...', timestamp: '2026-07-17 16:18:03 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-SGA-7C91D3', category: 'SGA Executive', summary: '1st: Marcus Thorne & Liam O\'Connor, 2nd: Elena Rostova...', timestamp: '2026-07-17 16:12:49 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-ENG-9B42E1', category: 'Engineering Senate', summary: 'Choice: Devon Carter (MechE)', timestamp: '2026-07-17 16:05:22 UTC', status: '✅ Validated & Sealed' },
    { token: '#UV-2026-REF-2F81A4', category: 'Referendum 104', summary: 'Choice: 👍 YES - Approve Safety & Shuttle Expansion', timestamp: '2026-07-17 15:58:31 UTC', status: '✅ Validated & Sealed' }
];

// Load from LocalStorage or initialize
function getElections() {
    const saved = localStorage.getItem('univote_elections');
    if (saved) {
        try { return JSON.parse(saved); } catch(e) {}
    }
    localStorage.setItem('univote_elections', JSON.stringify(INITIAL_ELECTIONS));
    return JSON.parse(JSON.stringify(INITIAL_ELECTIONS));
}

function saveElections(elections) {
    localStorage.setItem('univote_elections', JSON.stringify(elections));
}

function getTownhall() {
    const saved = localStorage.getItem('univote_townhall');
    if (saved) {
        try { return JSON.parse(saved); } catch(e) {}
    }
    localStorage.setItem('univote_townhall', JSON.stringify(INITIAL_TOWNHALL));
    return JSON.parse(JSON.stringify(INITIAL_TOWNHALL));
}

function saveTownhall(townhall) {
    localStorage.setItem('univote_townhall', JSON.stringify(townhall));
}

function getAuditLedger() {
    const saved = localStorage.getItem('univote_ledger');
    if (saved) {
        try { return JSON.parse(saved); } catch(e) {}
    }
    localStorage.setItem('univote_ledger', JSON.stringify(INITIAL_LEDGER));
    return JSON.parse(JSON.stringify(INITIAL_LEDGER));
}

function saveAuditLedger(ledger) {
    localStorage.setItem('univote_ledger', JSON.stringify(ledger));
}

function resetAllData() {
    localStorage.setItem('univote_elections', JSON.stringify(INITIAL_ELECTIONS));
    localStorage.setItem('univote_townhall', JSON.stringify(INITIAL_TOWNHALL));
    localStorage.setItem('univote_ledger', JSON.stringify(INITIAL_LEDGER));
    window.location.reload();
}
