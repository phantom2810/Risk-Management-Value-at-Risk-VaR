import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Container,
  Grid,
  Card,
  CardContent,
  Button,
  Box,
  Paper
} from '@mui/material';
import {
  TrendingUp,
  Assessment,
  Portfolio,
  CloudUpload
} from '@mui/icons-material';

const Dashboard: React.FC = () => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Risk Management & VaR Calculator
          </Typography>
        </Toolbar>
      </AppBar>
      
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          {/* Welcome Section */}
          <Grid item xs={12}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h4" gutterBottom>
                Welcome to VaR Calculator
              </Typography>
              <Typography variant="body1" color="text.secondary">
                Calculate Value-at-Risk for your investment portfolios using Historical, 
                Parametric, and Monte Carlo methods.
              </Typography>
            </Paper>
          </Grid>
          
          {/* Quick Actions */}
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Portfolio sx={{ mr: 2, color: 'primary.main' }} />
                  <Typography variant="h6">Create Portfolio</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Create a new investment portfolio to start risk analysis
                </Typography>
                <Button variant="contained" fullWidth>
                  Create Portfolio
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <CloudUpload sx={{ mr: 2, color: 'secondary.main' }} />
                  <Typography variant="h6">Upload Positions</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Upload portfolio positions from CSV file
                </Typography>
                <Button variant="contained" color="secondary" fullWidth>
                  Upload CSV
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <TrendingUp sx={{ mr: 2, color: 'success.main' }} />
                  <Typography variant="h6">Calculate VaR</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Run VaR calculations using different methodologies
                </Typography>
                <Button variant="contained" color="success" fullWidth>
                  Calculate VaR
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Assessment sx={{ mr: 2, color: 'warning.main' }} />
                  <Typography variant="h6">View Reports</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  View and download risk analysis reports
                </Typography>
                <Button variant="contained" color="warning" fullWidth>
                  View Reports
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          {/* Recent Activity */}
          <Grid item xs={12} md={8}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Recent VaR Calculations
              </Typography>
              <Typography variant="body2" color="text.secondary">
                No recent calculations. Create a portfolio and start calculating VaR.
              </Typography>
            </Paper>
          </Grid>
          
          {/* Risk Summary */}
          <Grid item xs={12} md={4}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Risk Summary
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Portfolio risk metrics will appear here after calculations.
              </Typography>
            </Paper>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
};

export default Dashboard;
