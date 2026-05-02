const TAX_RATE = 0.07;

const USERS = [
    { id: "USR-001", name: "Saw Ke Blute", role: "Owner", username: "owner", password: "owner123" },
    { id: "USR-002", name: "Samantha Jawjong", role: "Manager", username: "manager", password: "manager123" },
    { id: "USR-003", name: "Saw Joshua", role: "Cashier", username: "cashier", password: "cashier123" },
    { id: "USR-004", name: "Saw Eh Thalay Htoo", role: "Kitchen Staff", username: "kitchen", password: "kitchen123" },
    { id: "USR-005", name: "Similosakhe Moyo", role: "Inventory Staff", username: "inventory", password: "inventory123" }
];

const ROLE_PERMISSIONS = {
    Owner: ["dashboard", "menu", "pos", "orders", "inventory", "reports", "audit", "settings"],
    Manager: ["dashboard", "menu", "orders", "inventory", "reports"],
    Cashier: ["dashboard", "pos", "orders"],
    "Kitchen Staff": ["orders"],
    "Inventory Staff": ["dashboard", "inventory", "reports"]
};

const IMG = {
    friedNoodles: "assets/thai-fried-noodles.png",
    padThai: "assets/chicken-pad-thai.png",
    friedRice: "assets/seafood-fried-rice.png",
    tomYum: "assets/tom-yum-seafood-soup.png",
    suki: "assets/thai-suki-seafood.png",
    noodleSoup: "assets/tom-yum-seafood-soup.png",
    radNa: "assets/rad-na-pork-noodles.png",
    basil: "assets/spicy-basil-pork-rice.png",
    omelet: "assets/thai-omelet-rice.png",
    somTam: "assets/som-tam-thai.png"
};

const recipes = {
    porkRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-011", amount: 0.06 }],
    chickenRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-003", amount: 0.12 }, { ingredientId: "ING-011", amount: 0.06 }],
    beefRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-011", amount: 0.06 }],
    seafoodRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-011", amount: 0.06 }],
    vegRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-012", amount: 0.12 }, { ingredientId: "ING-011", amount: 0.05 }],
    mixRice: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.07 }, { ingredientId: "ING-003", amount: 0.07 }, { ingredientId: "ING-005", amount: 0.06 }],
    porkNoodle: [{ ingredientId: "ING-006", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.08 }],
    chickenNoodle: [{ ingredientId: "ING-006", amount: 0.18 }, { ingredientId: "ING-003", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.08 }],
    beefNoodle: [{ ingredientId: "ING-006", amount: 0.18 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.08 }],
    seafoodNoodle: [{ ingredientId: "ING-006", amount: 0.18 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-012", amount: 0.08 }],
    tomYumPork: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-014", amount: 0.05 }],
    tomYumChicken: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-003", amount: 0.12 }, { ingredientId: "ING-014", amount: 0.05 }],
    tomYumBeef: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-014", amount: 0.05 }],
    tomYumSeafood: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-014", amount: 0.05 }],
    sukiPork: [{ ingredientId: "ING-007", amount: 0.16 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-015", amount: 0.05 }, { ingredientId: "ING-012", amount: 0.12 }],
    sukiChicken: [{ ingredientId: "ING-007", amount: 0.16 }, { ingredientId: "ING-003", amount: 0.12 }, { ingredientId: "ING-015", amount: 0.05 }, { ingredientId: "ING-012", amount: 0.12 }],
    sukiBeef: [{ ingredientId: "ING-007", amount: 0.16 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-015", amount: 0.05 }, { ingredientId: "ING-012", amount: 0.12 }],
    sukiSeafood: [{ ingredientId: "ING-007", amount: 0.16 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-015", amount: 0.05 }, { ingredientId: "ING-012", amount: 0.12 }],
    soupPork: [{ ingredientId: "ING-008", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.1 }],
    soupBeef: [{ ingredientId: "ING-008", amount: 0.18 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.1 }],
    radNaPork: [{ ingredientId: "ING-006", amount: 0.2 }, { ingredientId: "ING-002", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.12 }],
    radNaChicken: [{ ingredientId: "ING-006", amount: 0.2 }, { ingredientId: "ING-003", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.12 }],
    radNaBeef: [{ ingredientId: "ING-006", amount: 0.2 }, { ingredientId: "ING-004", amount: 0.12 }, { ingredientId: "ING-012", amount: 0.12 }],
    radNaSeafood: [{ ingredientId: "ING-006", amount: 0.2 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-012", amount: 0.12 }],
    basilPork: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-002", amount: 0.13 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    basilChicken: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-003", amount: 0.13 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    basilBeef: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-004", amount: 0.13 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    basilSeafood: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-005", amount: 0.14 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    basilOctopus: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-017", amount: 0.14 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    basilShrimp: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-018", amount: 0.14 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    preservedEgg: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-019", amount: 1 }, { ingredientId: "ING-010", amount: 0.25 }, { ingredientId: "ING-011", amount: 0.06 }],
    friedEgg: [{ ingredientId: "ING-009", amount: 1 }],
    omelet: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-009", amount: 2 }, { ingredientId: "ING-011", amount: 0.03 }],
    omeletPork: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-009", amount: 2 }, { ingredientId: "ING-002", amount: 0.08 }],
    omeletChicken: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-009", amount: 2 }, { ingredientId: "ING-003", amount: 0.08 }],
    omeletShrimp: [{ ingredientId: "ING-001", amount: 0.18 }, { ingredientId: "ING-009", amount: 2 }, { ingredientId: "ING-018", amount: 0.08 }],
    somTam: [{ ingredientId: "ING-020", amount: 0.25 }, { ingredientId: "ING-021", amount: 0.08 }, { ingredientId: "ING-011", amount: 0.03 }],
    somTamCorn: [{ ingredientId: "ING-022", amount: 0.25 }, { ingredientId: "ING-021", amount: 0.08 }, { ingredientId: "ING-011", amount: 0.03 }]
};

const menuItems = [
    ["Fried Noodles with Pork", "ผัดซีอิ๊วหมู", "Fried Noodles", 40, 18, IMG.friedNoodles, recipes.porkNoodle],
    ["Fried Noodles with Beef", "ผัดซีอิ๊วเนื้อ", "Fried Noodles", 50, 25, IMG.friedNoodles, recipes.beefNoodle],
    ["Fried Noodles with Chicken", "ผัดซีอิ๊วไก่", "Fried Noodles", 40, 18, IMG.friedNoodles, recipes.chickenNoodle],
    ["Fried Noodles with Seafood", "ผัดซีอิ๊วทะเล", "Fried Noodles", 50, 28, IMG.friedNoodles, recipes.seafoodNoodle],
    ["Pad Thai with Pork", "ผัดไทยหมู", "Pad Thai", 40, 18, IMG.padThai, recipes.porkNoodle],
    ["Pad Thai with Chicken", "ผัดไทยไก่", "Pad Thai", 40, 18, IMG.padThai, recipes.chickenNoodle],
    ["Pad Thai with Beef", "ผัดไทยเนื้อ", "Pad Thai", 50, 25, IMG.padThai, recipes.beefNoodle],
    ["Pad Thai with Seafood", "ผัดไทยทะเล", "Pad Thai", 50, 28, IMG.padThai, recipes.seafoodNoodle],
    ["Fried Rice with Pork", "ข้าวผัดหมู", "Fried Rice", 40, 18, IMG.friedRice, recipes.porkRice],
    ["Fried Rice with Chicken", "ข้าวผัดไก่", "Fried Rice", 40, 18, IMG.friedRice, recipes.chickenRice],
    ["Fried Rice with Seafood", "ข้าวผัดทะเล", "Fried Rice", 50, 28, IMG.friedRice, recipes.seafoodRice],
    ["Fried Rice with Beef", "ข้าวผัดเนื้อ", "Fried Rice", 50, 25, IMG.friedRice, recipes.beefRice],
    ["Fried Rice with Vegetables", "ข้าวผัดผัก", "Fried Rice", 40, 14, IMG.friedRice, recipes.vegRice],
    ["Fried Rice with Mixed Meat", "ข้าวผัดรวม", "Fried Rice", 50, 28, IMG.friedRice, recipes.mixRice],
    ["Stir-Fried Chili with Beef", "ผัดพริกเผาเนื้อ", "Chili Stir-Fry", 50, 25, IMG.basil, recipes.basilBeef],
    ["Stir-Fried Chili with Chicken", "ผัดพริกเผาไก่", "Chili Stir-Fry", 40, 18, IMG.basil, recipes.basilChicken],
    ["Stir-Fried Chili with Pork", "ผัดพริกเผาหมู", "Chili Stir-Fry", 40, 18, IMG.basil, recipes.basilPork],
    ["Tom Yum Fried Rice", "ข้าวผัดต้มยำ", "Tom Yum Fried Rice", 40, 18, IMG.tomYum, recipes.tomYumChicken],
    ["Tom Yum Fried Rice with Seafood", "ข้าวผัดต้มยำทะเล", "Tom Yum Fried Rice", 50, 29, IMG.tomYum, recipes.tomYumSeafood],
    ["Tom Yum Fried Rice with Pork", "ข้าวผัดต้มยำหมู", "Tom Yum Fried Rice", 40, 18, IMG.tomYum, recipes.tomYumPork],
    ["Tom Yum Fried Rice with Chicken", "ข้าวผัดต้มยำไก่", "Tom Yum Fried Rice", 40, 18, IMG.tomYum, recipes.tomYumChicken],
    ["Tom Yum Fried Rice with Beef", "ข้าวผัดต้มยำเนื้อ", "Tom Yum Fried Rice", 50, 25, IMG.tomYum, recipes.tomYumBeef],
    ["Suki Soup", "สุกี้น้ำ", "Suki", 40, 18, IMG.suki, recipes.sukiChicken],
    ["Suki Seafood Soup", "สุกี้ทะเลน้ำ", "Suki", 50, 29, IMG.suki, recipes.sukiSeafood],
    ["Suki Seafood Dry", "สุกี้ทะเลแห้ง", "Suki", 50, 29, IMG.suki, recipes.sukiSeafood],
    ["Suki Pork Soup", "สุกี้หมูน้ำ", "Suki", 40, 18, IMG.suki, recipes.sukiPork],
    ["Suki Pork Dry", "สุกี้หมูแห้ง", "Suki", 40, 18, IMG.suki, recipes.sukiPork],
    ["Suki Beef Soup", "สุกี้เนื้อน้ำ", "Suki", 50, 25, IMG.suki, recipes.sukiBeef],
    ["Suki Beef Dry", "สุกี้เนื้อแห้ง", "Suki", 50, 25, IMG.suki, recipes.sukiBeef],
    ["Suki Chicken Soup", "สุกี้ไก่น้ำ", "Suki", 40, 18, IMG.suki, recipes.sukiChicken],
    ["Suki Chicken Dry", "สุกี้ไก่แห้ง", "Suki", 40, 18, IMG.suki, recipes.sukiChicken],
    ["Pork Noodles", "ก๋วยเตี๋ยวหมู", "Noodle Soup", 40, 18, IMG.noodleSoup, recipes.soupPork],
    ["Beef Noodles", "ก๋วยเตี๋ยวเนื้อ", "Noodle Soup", 50, 25, IMG.noodleSoup, recipes.soupBeef],
    ["Tom Yum Pork Noodles", "ก๋วยเตี๋ยวต้มยำหมู", "Noodle Soup", 40, 18, IMG.noodleSoup, recipes.tomYumPork],
    ["Tom Yum Beef Noodles", "ก๋วยเตี๋ยวต้มยำเนื้อ", "Noodle Soup", 50, 25, IMG.noodleSoup, recipes.tomYumBeef],
    ["Yen Ta Fo Pork Noodles", "เย็นตาโฟหมู", "Noodle Soup", 40, 18, IMG.noodleSoup, recipes.soupPork],
    ["Yen Ta Fo Beef Noodles", "เย็นตาโฟเนื้อ", "Noodle Soup", 50, 25, IMG.noodleSoup, recipes.soupBeef],
    ["Rad Na with Pork", "ราดหน้าหมู", "Rad Na", 40, 18, IMG.radNa, recipes.radNaPork],
    ["Rad Na with Beef", "ราดหน้าเนื้อ", "Rad Na", 50, 25, IMG.radNa, recipes.radNaBeef],
    ["Rad Na with Chicken", "ราดหน้าไก่", "Rad Na", 40, 18, IMG.radNa, recipes.radNaChicken],
    ["Rad Na with Seafood", "ราดหน้าทะเล", "Rad Na", 50, 29, IMG.radNa, recipes.radNaSeafood],
    ["Spicy Basil Pork with Rice", "ผัดกะเพราหมู", "Basil Stir-Fry", 40, 18, IMG.basil, recipes.basilPork],
    ["Spicy Basil Beef with Rice", "ผัดกะเพราเนื้อ", "Basil Stir-Fry", 50, 25, IMG.basil, recipes.basilBeef],
    ["Spicy Basil Chicken with Rice", "ผัดกะเพราไก่", "Basil Stir-Fry", 40, 18, IMG.basil, recipes.basilChicken],
    ["Spicy Basil Seafood with Rice", "ผัดกะเพราทะเล", "Basil Stir-Fry", 50, 29, IMG.basil, recipes.basilSeafood],
    ["Spicy Basil Octopus with Rice", "ผัดกะเพราปลาหมึก", "Basil Stir-Fry", 50, 29, IMG.basil, recipes.basilOctopus],
    ["Spicy Basil Shrimp with Rice", "ผัดกะเพรากุ้ง", "Basil Stir-Fry", 50, 29, IMG.basil, recipes.basilShrimp],
    ["Spicy Basil Preserved Egg with Rice", "ผัดกะเพราไข่เยี่ยวม้า", "Basil Stir-Fry", 50, 24, IMG.basil, recipes.preservedEgg],
    ["Fried Egg", "ไข่ดาว", "Egg & Omelet", 10, 5, IMG.omelet, recipes.friedEgg],
    ["Omelet Rice", "ข้าวไข่เจียว", "Egg & Omelet", 30, 12, IMG.omelet, recipes.omelet],
    ["Pork Omelet with Rice", "ข้าวไข่เจียวหมูสับ", "Egg & Omelet", 40, 18, IMG.omelet, recipes.omeletPork],
    ["Chicken Omelet with Rice", "ข้าวไข่เจียวไก่", "Egg & Omelet", 40, 18, IMG.omelet, recipes.omeletChicken],
    ["Vegetable Omelet with Rice", "ข้าวไข่เจียวผัก", "Egg & Omelet", 40, 15, IMG.omelet, recipes.omelet],
    ["Shrimp Omelet with Rice", "ข้าวไข่เจียวกุ้ง", "Egg & Omelet", 50, 26, IMG.omelet, recipes.omeletShrimp],
    ["Tam Pa", "ตำป่า", "Som Tam", 50, 22, IMG.somTam, recipes.somTam],
    ["Tam Pla Ra", "ตำปลาร้า", "Som Tam", 40, 16, IMG.somTam, recipes.somTam],
    ["Tam Thai", "ตำไทย", "Som Tam", 40, 16, IMG.somTam, recipes.somTam],
    ["Tam Thai Salted Egg", "ตำไทยไข่เค็ม", "Som Tam", 50, 23, IMG.somTam, recipes.somTam],
    ["Tam Sua", "ตำซั่ว", "Som Tam", 40, 16, IMG.somTam, recipes.somTam],
    ["Tam Khao Pod", "ตำข้าวโพด", "Som Tam", 40, 16, IMG.somTam, recipes.somTamCorn],
    ["Tam Khao Pod Salted Egg", "ตำข้าวโพดไข่เค็ม", "Som Tam", 50, 23, IMG.somTam, recipes.somTamCorn],
    ["Tam Korat", "ตำโคราช", "Som Tam", 50, 22, IMG.somTam, recipes.somTam],
    ["Tam Zab", "ตำแซ่บ", "Som Tam", 40, 16, IMG.somTam, recipes.somTam]
];

const MENU_DATA = menuItems.map(([name, thaiName, category, price, cost, image, recipe], index) => ({
    id: index + 1,
    name,
    thaiName,
    category,
    price,
    cost,
    image,
    recipe,
    description: `${thaiName} - ${name}. Shop menu item prepared to order.`
}));

const CATEGORIES = [
    "All",
    "Fried Noodles",
    "Pad Thai",
    "Fried Rice",
    "Chili Stir-Fry",
    "Tom Yum Fried Rice",
    "Suki",
    "Noodle Soup",
    "Rad Na",
    "Basil Stir-Fry",
    "Egg & Omelet",
    "Som Tam"
];

const INITIAL_INVENTORY = [
    { id: "ING-001", name: "Jasmine Rice", unit: "kg", quantity: 45, reorderLevel: 12, costPerUnit: 38 },
    { id: "ING-002", name: "Pork", unit: "kg", quantity: 22, reorderLevel: 6, costPerUnit: 105 },
    { id: "ING-003", name: "Chicken", unit: "kg", quantity: 22, reorderLevel: 6, costPerUnit: 95 },
    { id: "ING-004", name: "Beef", unit: "kg", quantity: 12, reorderLevel: 4, costPerUnit: 210 },
    { id: "ING-005", name: "Mixed Seafood", unit: "kg", quantity: 14, reorderLevel: 5, costPerUnit: 220 },
    { id: "ING-006", name: "Wide Rice Noodles", unit: "kg", quantity: 20, reorderLevel: 6, costPerUnit: 45 },
    { id: "ING-007", name: "Glass Noodles", unit: "kg", quantity: 14, reorderLevel: 4, costPerUnit: 55 },
    { id: "ING-008", name: "Soup Noodles", unit: "kg", quantity: 18, reorderLevel: 5, costPerUnit: 42 },
    { id: "ING-009", name: "Eggs", unit: "pcs", quantity: 90, reorderLevel: 24, costPerUnit: 5 },
    { id: "ING-010", name: "Holy Basil", unit: "bunches", quantity: 26, reorderLevel: 8, costPerUnit: 12 },
    { id: "ING-011", name: "Chili and Garlic", unit: "kg", quantity: 9, reorderLevel: 3, costPerUnit: 70 },
    { id: "ING-012", name: "Mixed Vegetables", unit: "kg", quantity: 20, reorderLevel: 6, costPerUnit: 45 },
    { id: "ING-013", name: "Pad Thai Sauce", unit: "liters", quantity: 7, reorderLevel: 2, costPerUnit: 90 },
    { id: "ING-014", name: "Tom Yum Paste and Herbs", unit: "kg", quantity: 7, reorderLevel: 2, costPerUnit: 120 },
    { id: "ING-015", name: "Suki Sauce", unit: "liters", quantity: 8, reorderLevel: 2, costPerUnit: 80 },
    { id: "ING-016", name: "Rad Na Gravy Mix", unit: "kg", quantity: 8, reorderLevel: 2, costPerUnit: 75 },
    { id: "ING-017", name: "Octopus", unit: "kg", quantity: 7, reorderLevel: 2, costPerUnit: 230 },
    { id: "ING-018", name: "Shrimp", unit: "kg", quantity: 8, reorderLevel: 3, costPerUnit: 240 },
    { id: "ING-019", name: "Preserved Eggs", unit: "pcs", quantity: 24, reorderLevel: 8, costPerUnit: 12 },
    { id: "ING-020", name: "Green Papaya", unit: "kg", quantity: 18, reorderLevel: 6, costPerUnit: 35 },
    { id: "ING-021", name: "Som Tam Seasoning", unit: "kg", quantity: 8, reorderLevel: 3, costPerUnit: 95 },
    { id: "ING-022", name: "Corn", unit: "kg", quantity: 10, reorderLevel: 3, costPerUnit: 45 }
];

const RECIPES = Object.fromEntries(MENU_DATA.map(item => [item.id, item.recipe]));

const INITIAL_ORDERS = [
    {
        id: "ORD-7281",
        userId: "USR-003",
        items: [
            { menuItemId: 42, name: "Spicy Basil Pork with Rice", quantity: 1, price: 40, cost: 18 },
            { menuItemId: 49, name: "Fried Egg", quantity: 1, price: 10, cost: 5 }
        ],
        subtotal: 50,
        tax: 3.50,
        total: 53.50,
        status: "Preparing",
        paymentStatus: "Paid",
        createdAt: new Date().toISOString()
    },
    {
        id: "ORD-7280",
        userId: "USR-003",
        items: [{ menuItemId: 11, name: "Fried Rice with Seafood", quantity: 1, price: 50, cost: 28 }],
        subtotal: 50,
        tax: 3.50,
        total: 53.50,
        status: "Ready",
        paymentStatus: "Paid",
        createdAt: new Date(Date.now() - 900000).toISOString()
    }
];

export {
    TAX_RATE,
    USERS,
    ROLE_PERMISSIONS,
    MENU_DATA,
    CATEGORIES,
    INITIAL_INVENTORY,
    RECIPES,
    INITIAL_ORDERS
};
