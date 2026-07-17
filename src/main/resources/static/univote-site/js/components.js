/**
 * UniVote AI — Shared Multi-Page Navigation & Footer Component
 * Automatically renders standard header and navigation bars on all standalone pages.
 */

function renderHeader(activePage) {
    const headerElem = document.getElementById('shared-header');
    if (!headerElem) return;

    headerElem.innerHTML = `
        <!-- Top Announcement Banner -->
        <div class="bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white text-xs sm:text-sm font-semibold py-2 px-4 shadow-lg flex items-center justify-between z-50 relative">
            <div class="flex items-center space-x-2 max-w-7xl mx-auto w-full justify-between">
                <div class="flex items-center space-x-2">
                    <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-bold bg-white/20 uppercase tracking-wide">Live Campus Audit</span>
                    <span>🔥 Spring 2026 Student Government & Senate Elections are open! Cast verifiable Ranked-Choice ballots before 11:59 PM.</span>
                </div>
                <div class="hidden md:flex items-center space-x-4 text-xs font-medium bg-black/20 px-3 py-1 rounded-full border border-white/15">
                    <span class="flex items-center"><span class="w-2 h-2 rounded-full bg-emerald-400 inline-block mr-1.5 pulse-dot"></span> Cryptographic Ledger: Online</span>
                    <span>•</span>
                    <span>Turnout: <strong class="text-amber-300">68.4%</strong></span>
                </div>
            </div>
        </div>

        <!-- Main Glass Navbar -->
        <header class="sticky top-0 z-40 glass-navbar transition-colors">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex items-center justify-between h-20">
                    <!-- Logo -->
                    <a href="index.html" class="flex items-center space-x-3 text-white no-underline">
                        <div class="w-11 h-11 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-500 flex items-center justify-center badge-glow text-white font-extrabold text-xl tracking-tighter">
                            UV
                        </div>
                        <div>
                            <div class="flex items-center space-x-2">
                                <span class="font-extrabold text-xl sm:text-2xl tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-white via-indigo-100 to-indigo-300">UniVote<span class="text-indigo-400">.ai</span></span>
                                <span class="bg-indigo-500/15 border border-indigo-500/30 text-indigo-300 text-[10px] font-bold px-2 py-0.5 rounded-full uppercase tracking-wider hidden sm:inline-block">College Edition</span>
                            </div>
                            <p class="text-xs text-slate-400 hidden sm:block">Verifiable • Ranked-Choice • Zero-Knowledge</p>
                        </div>
                    </a>

                    <!-- Role Switcher & Student Status -->
                    <div class="flex items-center space-x-3 sm:space-x-4">
                        <div class="bg-slate-800/90 p-1 rounded-xl border border-slate-700/60 flex items-center shadow-inner text-xs sm:text-sm font-medium">
                            <a href="ballots.html" class="px-3 py-1.5 rounded-lg transition-all flex items-center space-x-1.5 ${activePage === 'ballots' || activePage === 'compare' || activePage === 'townhall' || activePage === 'ledger' || activePage === 'dashboard' || activePage === 'rcv' ? 'bg-indigo-600 text-white shadow-md font-semibold' : 'text-slate-300 hover:text-white'}">
                                <span>🎓</span>
                                <span class="hidden md:inline">Student Voter</span>
                            </a>
                            <a href="candidate-portal.html" class="px-3 py-1.5 rounded-lg transition-all flex items-center space-x-1.5 ${activePage === 'candidate' ? 'bg-indigo-600 text-white shadow-md font-semibold' : 'text-slate-300 hover:text-white'}">
                                <span>🏆</span>
                                <span class="hidden md:inline">Candidate Portal</span>
                            </a>
                            <a href="admin-portal.html" class="px-3 py-1.5 rounded-lg transition-all flex items-center space-x-1.5 ${activePage === 'admin' ? 'bg-indigo-600 text-white shadow-md font-semibold' : 'text-slate-300 hover:text-white'}">
                                <span>🛡️</span>
                                <span class="hidden md:inline">Election Commission</span>
                            </a>
                        </div>

                        <!-- Student Verification Badge -->
                        <div class="hidden lg:flex items-center space-x-2 pl-2 border-l border-slate-700/80">
                            <div class="w-8 h-8 rounded-full bg-gradient-to-br from-emerald-500 to-teal-600 flex items-center justify-center text-white font-bold text-xs shadow-sm">
                                JS
                            </div>
                            <div class="text-left text-xs">
                                <div class="font-bold text-slate-200">Jordan Smith</div>
                                <div class="text-emerald-400 font-mono text-[11px] flex items-center">
                                    <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 mr-1"></span> Verified • Senior (CS)
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Primary Page Navigation Bar -->
                <nav class="flex space-x-2 sm:space-x-3 overflow-x-auto py-3 no-scrollbar border-t border-slate-800/60 text-xs sm:text-sm font-semibold">
                    <a href="ballots.html" class="nav-link-item ${activePage === 'ballots' ? 'active' : ''}">
                        <span>🗳️</span>
                        <span>Active Elections & Ballots</span>
                    </a>
                    <a href="compare.html" class="nav-link-item ${activePage === 'compare' ? 'active' : ''}">
                        <span>📊</span>
                        <span>Platform Comparison Matrix</span>
                    </a>
                    <a href="townhall.html" class="nav-link-item ${activePage === 'townhall' ? 'active' : ''}">
                        <span>💬</span>
                        <span>Townhall Q&A</span>
                        <span class="bg-indigo-500/20 text-indigo-300 px-2 py-0.5 rounded-full text-[10px]">16</span>
                    </a>
                    <a href="ledger.html" class="nav-link-item ${activePage === 'ledger' ? 'active' : ''}">
                        <span>🔐</span>
                        <span>Public Audit & Verification</span>
                    </a>
                    <a href="dashboard.html" class="nav-link-item ${activePage === 'dashboard' ? 'active' : ''}">
                        <span>📈</span>
                        <span>Live Turnout Analytics</span>
                    </a>
                    <a href="rcv-simulator.html" class="nav-link-item ${activePage === 'rcv' ? 'active' : ''}">
                        <span>⚡</span>
                        <span>RCV Runoff Simulator</span>
                    </a>
                </nav>
            </div>
        </header>
    `;
}

function renderFooter() {
    const footerElem = document.getElementById('shared-footer');
    if (!footerElem) return;

    footerElem.innerHTML = `
        <footer class="border-t border-slate-800/80 bg-slate-900/40 py-8 text-xs text-slate-400 mt-16">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4">
                <div class="flex items-center space-x-2">
                    <span class="font-extrabold text-white">UniVote AI College System</span>
                    <span>•</span>
                    <span>Multi-Page & Standalone Web Architecture • Built for Campus Governance</span>
                </div>
                <div class="flex items-center space-x-4">
                    <a href="index.html" class="text-indigo-400 hover:underline">Portal Hub</a>
                    <span>•</span>
                    <button onclick="resetAllData()" class="text-slate-400 hover:text-white">Reset Sample Data</button>
                    <span>•</span>
                    <span>🔐 SHA-256 Verified</span>
                </div>
            </div>
        </footer>
    `;
}
