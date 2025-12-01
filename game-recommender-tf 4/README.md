# Game Recommender AI - TensorFlow Edition

A neural network-based game recommendation system using **TensorFlow Java**.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                     TWO-TOWER NEURAL NETWORK MODEL                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   USER TOWER                              GAME TOWER                    │
│   ──────────                              ──────────                    │
│                                                                         │
│   ┌─────────────┐                         ┌─────────────┐              │
│   │ User Input  │                         │ Game Input  │              │
│   │ (Features)  │                         │ (Features)  │              │
│   └──────┬──────┘                         └──────┬──────┘              │
│          │ [N features]                          │ [N features]        │
│          ▼                                       ▼                      │
│   ┌─────────────┐                         ┌─────────────┐              │
│   │  Dense 128  │                         │  Dense 128  │              │
│   │    ReLU     │                         │    ReLU     │              │
│   └──────┬──────┘                         └──────┬──────┘              │
│          │                                       │                      │
│          ▼                                       ▼                      │
│   ┌─────────────┐                         ┌─────────────┐              │
│   │  Dense 64   │                         │  Dense 64   │              │
│   │    ReLU     │                         │    ReLU     │              │
│   └──────┬──────┘                         └──────┬──────┘              │
│          │                                       │                      │
│          ▼                                       ▼                      │
│   ┌─────────────┐                         ┌─────────────┐              │
│   │  Dense 32   │                         │  Dense 32   │              │
│   │ (Embedding) │                         │ (Embedding) │              │
│   └──────┬──────┘                         └──────┬──────┘              │
│          │                                       │                      │
│          ▼                                       ▼                      │
│   ┌─────────────┐                         ┌─────────────┐              │
│   │ L2 Normalize│                         │ L2 Normalize│              │
│   └──────┬──────┘                         └──────┬──────┘              │
│          │                                       │                      │
│          └───────────────┬───────────────────────┘                      │
│                          │                                              │
│                          ▼                                              │
│                   ┌─────────────┐                                       │
│                   │ Dot Product │  ← Cosine Similarity                  │
│                   │ (Similarity)│                                       │
│                   └──────┬──────┘                                       │
│                          │                                              │
│                          ▼                                              │
│                   ┌─────────────┐                                       │
│                   │   Score     │  → Recommendation Ranking             │
│                   │   [0, 1]    │                                       │
│                   └─────────────┘                                       │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## TensorFlow Java API Usage

### Key TensorFlow Operations Used

```java
// Creating computation graph
Graph graph = new Graph();
Ops tf = Ops.create(graph);

// Matrix multiplication (Dense layer)
Operand<TFloat32> output = tf.linalg.matMul(input, weights);

// Bias addition
output = tf.math.add(output, bias);

// ReLU activation
output = tf.nn.relu(output);

// L2 Normalization
output = tf.nn.l2Normalize(output, tf.constant(new int[] {1}));

// Execute the graph
try (Session sess = new Session(graph)) {
    TFloat32 result = (TFloat32) sess.runner()
        .fetch(outputOp)
        .run()
        .get(0);
}
```

### Feature Encoding

Games and users are converted to tensors:

```java
// One-hot encoding for categorical features
float[] features = new float[featureSize];

// Genres: [0,0,1,0,0,1,...]  (multi-hot)
// Tags: [1,0,0,1,0,0,...]    (multi-hot)
// Difficulty: [0,1,0,0]       (one-hot)
// Platform: [1,0,0]           (one-hot)
// Numerical: [0.85, 0.7, 0.3, 0.5, 1.0] (normalized)
```

## How It Works

### 1. Training Phase

The model learns from user-game interactions:

```java
List<TrainingExample> examples = ...;  // (user, game, rating) tuples

model.train(examples, epochs=50, learningRate=0.01f);
```

Training uses Mean Squared Error (MSE) loss:
```
Loss = (predicted_similarity - actual_rating/10)²
```

### 2. Inference Phase

For recommendations:

1. Encode user preferences → User Tensor
2. Pass through neural network → User Embedding (32-dim vector)
3. Compare with precomputed game embeddings
4. Rank by cosine similarity
5. Return top-N games

```java
List<ScoredGame> recommendations = model.recommend(user, topN);
```

### 3. Similar Games

Uses the same embeddings to find similar games:

```java
List<ScoredGame> similar = model.findSimilar(targetGame, topN);
```

## Project Structure

```
game-recommender-tf/
├── pom.xml                          # Maven config with TensorFlow deps
├── src/main/java/com/gamerecommender/
│   ├── Main.java                    # Demo application
│   ├── model/
│   │   ├── Game.java                # Game entity
│   │   └── UserProfile.java         # User preferences
│   ├── data/
│   │   └── GameDatabase.java        # Placeholder games
│   └── ai/
│       ├── TensorFlowFeatureEncoder.java    # Feature encoding
│       └── TensorFlowRecommendationModel.java  # Neural network
```

## Dependencies

```xml
<!-- TensorFlow Java -->
<dependency>
    <groupId>org.tensorflow</groupId>
    <artifactId>tensorflow-core-platform</artifactId>
    <version>0.5.0</version>
</dependency>

<dependency>
    <groupId>org.tensorflow</groupId>
    <artifactId>tensorflow-framework</artifactId>
    <version>0.5.0</version>
</dependency>
```

## Building & Running

### Prerequisites
- **Java 17+** (JDK, not just JRE)
- **Maven 3.6+**

### Option 1: Use the run script
```bash
# Linux/macOS
chmod +x run.sh
./run.sh

# Windows
run.bat
```

### Option 2: Manual Maven commands
```bash
# Step 1: Compile (downloads TensorFlow dependencies ~200MB first time)
mvn clean compile

# Step 2: Run
mvn exec:java -Dexec.mainClass="com.gamerecommender.Main"
```

### Option 3: Create executable JAR
```bash
# Build fat JAR with all dependencies
mvn clean package

# Run the JAR
java -jar target/game-recommender-tf-1.0-SNAPSHOT.jar
```

### Common Issues

**ClassNotFoundException**: Make sure you're using `mvn exec:java` not `java -cp`

**Download stuck**: TensorFlow JARs are large (~200MB). First build takes time.

**Java version error**: Ensure JAVA_HOME points to JDK 17+
```bash
java -version   # Should show 17 or higher
```

## Model Hyperparameters

| Parameter | Value | Description |
|-----------|-------|-------------|
| Input Size | ~50 | Depends on vocabulary |
| Hidden Layer 1 | 128 | First dense layer |
| Hidden Layer 2 | 64 | Second dense layer |
| Embedding Size | 32 | Final embedding dimensions |
| Activation | ReLU | Hidden layer activation |
| Normalization | L2 | Embedding normalization |

## Comparison: Custom vs TensorFlow

| Aspect | Custom Algorithms | TensorFlow |
|--------|------------------|------------|
| Approach | Rule-based scoring | Neural network |
| Learning | Explicit formulas | Gradient descent |
| Embeddings | Hand-crafted | Learned |
| Scalability | Limited | Better with GPU |
| Complexity | Simple | More complex |
| Accuracy | Good baseline | Can be higher with data |

## Improvements to Consider

1. **Use Pre-trained Embeddings**: Word2Vec/BERT for text features
2. **Add Attention Mechanism**: For weighted feature importance
3. **Sequence Modeling**: Consider user's play order with LSTM/Transformer
4. **Collaborative Filtering**: Add user-user similarity tower
5. **GPU Acceleration**: Use TensorFlow GPU for faster training
6. **Model Serving**: Export to SavedModel for production deployment

## This Architecture Is Used By

- YouTube Recommendations
- Google Play Store
- Netflix
- Spotify
- Amazon Product Recommendations

The Two-Tower model is industry-standard for large-scale recommendation systems!
